INSERT INTO usuario (nome, email, senha, tipo) VALUES ('Carlos Silva', 'carlos@gradua.com', '123', 'PADRAO');
INSERT INTO usuario (nome, email, senha, tipo) VALUES ('Ana Beatriz', 'ana@gradua.com', '123', 'PADRAO');
INSERT INTO usuario (nome, email, senha, tipo) VALUES ('Admin', 'admin@gradua.com', 'admin', 'ADMIN');

INSERT INTO simulado (titulo) VALUES ('Simulado Oficial ENEM');

INSERT INTO questao (enunciado, id_simulado) VALUES ('Qual a capital da França?', 1);
INSERT INTO questao (enunciado, id_simulado) VALUES ('Quanto é 2 + 2?', 1);

INSERT INTO resultado (pontuacao, acertos, total_questoes, realizado_em, status, id_usuario, id_simulado) 
VALUES (100.0, 2, 2, '2023-10-01 10:00:00', 'FINALIZADO', 1, 1);