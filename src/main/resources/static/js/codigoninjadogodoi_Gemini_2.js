
const App = {

    CONFIG: {
        seletores: {
            money: ".componentemoney",
            data: ".componentedata",
            hora: ".componentehora",
            confirm: "[hx-confirm]:not([data-confirm-ready])",
            buscaContainer: ".searchable-select-container:not([data-initialized])",
            msgInput: "mensagemSA2",
            tipoInput: "tipoSA2",
            intervaloInput: "intervaloSA2",
        },
        maskMoney: {
            prefix: "",
            suffix: "",
            fixed: true,
            fractionDigits: 2,
            decimalSeparator: ",",
            thousandsSeparator: ".",
            autoCompleteDecimal: true,
        },
        datePicker: {
            dateFormat: "dd/mm/yyyy",
            customWeekDays: ["Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"],
            customMonths: ["Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"],
            customClearBTN: "Limpar",
            customCancelBTN: "Cancelar",
            theme: { theme_color: "rgb(31 41 55)" },
        },
        texto: {
            cancelar: "Cancelar",
            remover: "Remover",
            tituloConfirm: "Você tem certeza?",
        }
    },

    Toast: Swal.mixin({
        toast: true,
        position: "center-end",
        iconColor: "white",
        customClass: { popup: "colored-toast" },
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,
        grow: "row",
        didOpen: (toast) => {
            toast.onmouseenter = Swal.stopTimer;
            toast.onmouseleave = Swal.resumeTimer;
        },
    }),

    init: function () {
        this.atualizarComponentes();
        this.registrarListenersGlobais();
    },

    // Chama todas as preparações. Útil para chamar manualmente ou via HTMX.
    atualizarComponentes: function () {
        this.prepararMoney();
        this.prepararData();
        this.prepararHora();
        this.prepararConfirmacoes();
        this.prepararBusca();
        this.mostrarNotificacao();
    },

    registrarListenersGlobais: function () {
        if (typeof htmx !== 'undefined') {
            htmx.on("htmx:afterSettle", () => {
                this.atualizarComponentes();
            });
        }

        // Listener global para fechar dropdowns ao clicar fora (Singleton)
        if (!document.body.dataset.clickOutsideRegistered) {
            document.addEventListener("click", (event) => {
                const activeDropdown = document.querySelector(".custom-select-dropdown:not(.hidden)");
                // Se houver um dropdown aberto e o clique não foi dentro dele
                if (activeDropdown && !event.target.closest(".searchable-select-container")) {
                    // Fecha o dropdown
                    activeDropdown.classList.add("hidden");
                    // Remove o foco visual do trigger associado
                    const activeTrigger = activeDropdown.closest(".searchable-select-container").querySelector(".custom-select-trigger");
                    if (activeTrigger) {
                        this._removeFocus(activeTrigger);
                    }
                }
            });
            document.body.dataset.clickOutsideRegistered = "true";
        }
    },

    prepararMoney: function () {
        const inputs = document.querySelectorAll(this.CONFIG.seletores.money);
        inputs.forEach((input) => {
            SimpleMaskMoney.setMask(input, this.CONFIG.maskMoney);
            input.classList.remove("componentemoney");
        });
    },

    prepararData: function () {
        const inputs = document.querySelectorAll(this.CONFIG.seletores.data);
        inputs.forEach((input) => {
            input.classList.remove("componentedata");
            // Cria cópia da config base para não alterar a original
            let options = { ...this.CONFIG.datePicker, el: "#" + input.id };
            // Lógica condicional simplificada (DRY)
            if (input.value) {
                const parts = input.value.split("/");
                // new Date(ano, mesBase0, dia)
                options.selectedDate = new Date(+parts[2], parts[1] - 1, +parts[0]);
            }
            const datePicker = MCDatepicker.create(options);
            datePicker.onOpen(() => {
                if (input.value && input.value !== datePicker.getFormatedDate()) {
                    const parts = input.value.split("/");
                    datePicker.setFullDate(new Date(+parts[2], parts[1] - 1, +parts[0]));
                }
            });
        });
    },

    prepararHora: function () {
        const inputs = document.querySelectorAll(this.CONFIG.seletores.hora);
        inputs.forEach((input) => {
            input.classList.remove("componentehora");
            const dialog = new mdDateTimePicker.default({
                type: "time", mode: true, inner24: true, cancel: this.CONFIG.texto.cancelar,
            });
            input.addEventListener("click", () => dialog.toggle());
            dialog.trigger = input;
            input.addEventListener("onOk", () => {
                input.value = dialog.time.format("HH:mm");
            });
        });
    },

    prepararConfirmacoes: function () {
        const elementos = document.querySelectorAll(this.CONFIG.seletores.confirm);
        elementos.forEach((elemento) => {
            // Usamos .bind(this) se precisássemos acessar o App dentro do callback, 
            // mas aqui a função é estática.
            elemento.addEventListener("htmx:confirm", this._dispararSweetAlert);
            elemento.setAttribute("data-confirm-ready", "true"); // Marca como pronto
        });
    },

    _dispararSweetAlert: function (e) {
        e.preventDefault();
        Swal.fire({
            title: App.CONFIG.texto.tituloConfirm, // Acesso via App.CONFIG
            text: e.detail.question,
            icon: "warning",
            showCancelButton: true,
            cancelButtonText: App.CONFIG.texto.cancelar,
            confirmButtonText: App.CONFIG.texto.remover,
            confirmButtonColor: "#3085d6",
        }).then((result) => {
            if (result.isConfirmed) {
                e.detail.issueRequest(true);
            }
        });
    },

    mostrarNotificacao: function () {
        const inputMsg = document.getElementById(this.CONFIG.seletores.msgInput);
        if (inputMsg && inputMsg.value) {
            const tipo = document.getElementById(this.CONFIG.seletores.tipoInput)?.value || 'success';
            const intervalo = document.getElementById(this.CONFIG.seletores.intervaloInput)?.value || 3000;
            this.Toast.fire({ icon: tipo, title: inputMsg.value, timer: intervalo });
            inputMsg.value = "";
        }
    },

    // Funções utilitárias para gerenciar o foco visual
    _applyFocus: function (trigger) {
        trigger.classList.remove("border-gray-300");
        trigger.classList.add("ring-1", "border-gray-500", "ring-gray-500");
    },

    _removeFocus: function (trigger) {
        trigger.classList.remove("ring-1", "border-gray-500", "ring-gray-500");
        trigger.classList.add("border-gray-300");
    },

    prepararBusca: function () {
        const containers = document.querySelectorAll(this.CONFIG.seletores.buscaContainer);

        containers.forEach((container) => {
            container.dataset.initialized = "true";
            const trigger = container.querySelector(".custom-select-trigger");
            const dropdown = container.querySelector(".custom-select-dropdown");
            const optionsList = container.querySelector(".options-list");
            const selectedValueSpan = container.querySelector(".selected-value-span");
            const hiddenInput = container.querySelector(".hidden-select-input");

            if (trigger && dropdown) {
                trigger.addEventListener("click", (e) => {
                    const isCurrentlyOpen = !dropdown.classList.contains("hidden");
                    // 1. Fechar outros dropdowns e remover foco
                    document.querySelectorAll(".custom-select-dropdown:not(.hidden)").forEach(d => {
                        if (d !== dropdown) {
                            d.classList.add("hidden");
                            // Remove o foco visual de outros triggers
                            const otherTrigger = d.closest(".searchable-select-container")?.querySelector(".custom-select-trigger");
                            if (otherTrigger) {
                                this._removeFocus(otherTrigger);
                            }
                        }
                    });
                    dropdown.classList.toggle("hidden");
                    // 2. Adicionar/Remover Foco Visual no Trigger atual
                    if (!isCurrentlyOpen) {
                        // Se estava fechado e vai abrir: Aplica o foco
                        this._applyFocus(trigger);
                    } else {
                        // Se estava aberto e vai fechar: Remove o foco
                        this._removeFocus(trigger);
                    }
                    e.stopPropagation();
                    if (!isCurrentlyOpen) {
                        const searchInput = dropdown.querySelector('input[type="text"]');
                        if (searchInput) {
                            searchInput.focus();
                        }
                    }
                });
            }

            if (optionsList) {
                optionsList.addEventListener("click", (event) => {
                    const option = event.target.closest("li");
                    if (option) {
                        if (selectedValueSpan) selectedValueSpan.textContent = option.textContent.trim();
                        if (hiddenInput) hiddenInput.value = option.dataset.value;
                        if (dropdown) dropdown.classList.add("hidden");
                        const trigger = container.querySelector(".custom-select-trigger");
                        if (trigger) {
                            App._removeFocus(trigger);
                        }
                    }
                });
            }
        });
    }
};

// Inicialização ao carregar a página
document.addEventListener("DOMContentLoaded", () => App.init());


/* ===========================================================================
 VERSÃO COM DELEGAÇÃO DE EVENTOS GLOBAL (ALTERNATIVO)
===========================================================================
 Esta função substitui o método 'prepararBusca' (com 'forEach') e o listener 
 de 'click outside' no 'registrarListenersGlobais', tratando todos os eventos 
 de clique de forma centralizada e garantindo a correta gestão do FOCO visual.
=========================================================================== 

registrarListenersDeBuscaGlobal: function() { // Este método deve ser chamado via this._registrarBuscaPorDelegacao() em App.init()
  
  // Verifica se o listener já foi registrado (Opcional, mas útil para evitar duplicação)
  if (document.body.dataset.selectDelegateRegistered) return;
  
  document.addEventListener("click", (event) => {
    
    // A. Clicou no TRIGGER para abrir/fechar?
    const trigger = event.target.closest(".custom-select-trigger");
    if (trigger) {
      const container = trigger.closest(".searchable-select-container");
      const dropdown = container.querySelector(".custom-select-dropdown");
      const isCurrentlyOpen = !dropdown.classList.contains("hidden"); // Estado ANTES do toggle

      // 1. Fecha outros selects e remove o foco visual deles
      document.querySelectorAll(".custom-select-dropdown:not(.hidden)").forEach(d => {
        if (d !== dropdown) {
          d.classList.add("hidden");
          const otherTrigger = d.closest(".searchable-select-container")?.querySelector(".custom-select-trigger");
          if (otherTrigger) this._removeFocus(otherTrigger); // ✅ Remove foco dos outros
        }
      });

      // 2. Abre/Fecha o dropdown atual
      dropdown.classList.toggle("hidden");

      // 3. Adiciona/Remove o foco visual no trigger atual
      if (!isCurrentlyOpen) {
        this._applyFocus(trigger); // ✅ Aplica foco ao abrir
      } else {
        this._removeFocus(trigger); // ✅ Remove foco ao fechar
      }
      
      event.stopPropagation(); // Impede que o clique caia no caso C
      return; 
    }

    // B. Clicou em uma OPÇÃO (LI)?
    const option = event.target.closest(".options-list li");
    if (option) {
      const container = option.closest(".searchable-select-container");
      const selectedValueSpan = container.querySelector(".selected-value-span");
      const hiddenInput = container.querySelector(".hidden-select-input");
      const dropdown = container.querySelector(".custom-select-dropdown");
      const trigger = container.querySelector(".custom-select-trigger"); // Necessário para remover o foco

      if (selectedValueSpan) selectedValueSpan.textContent = option.textContent.trim();
      if (hiddenInput) hiddenInput.value = option.dataset.value;
      
      // Fecha o dropdown atual
      if (dropdown) dropdown.classList.add("hidden");
      
      // Remove o foco visual após a seleção
      if (trigger) this._removeFocus(trigger); // ✅ Remove foco após seleção
      
      return;
    }

    // C. Clicou FORA (Click Outside)?
    // Se não clicou num container de busca, fecha tudo que estiver aberto
    if (!event.target.closest(".searchable-select-container")) {
      const activeDropdowns = document.querySelectorAll(".custom-select-dropdown:not(.hidden)");
      
      activeDropdowns.forEach(d => {
        d.classList.add("hidden");
        
        // Remove o foco visual de cada trigger que for fechado
        const activeTrigger = d.closest(".searchable-select-container")?.querySelector(".custom-select-trigger");
        if (activeTrigger) this._removeFocus(activeTrigger); // ✅ Remove foco ao fechar fora
      });
    }
  });
  
  document.body.dataset.selectDelegateRegistered = "true"; // Marca como registrado
}
*/