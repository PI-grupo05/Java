create database dataryzer;
use dataryzer;

-- MODELAGEM BASICA, SEM RELACIONAMENTO COM AS TABELAS 
create table distribuidora (
id_distribuidora	int primary key auto_increment not null,
cnpj				varchar(50) not null,
nome				varchar(50) not null,
sigla				char(2) not null,
numero				varchar(15) not null,
codigo_associacao_master 	varchar(10) default (substring(replace(uuid(), '-', ''), 1, 10)) -- gera um codigo aleatorio de ate 10 caracteres para que seja a senha para o usuario criar sua conta no nosso site.
);

create table cidade(
id_cidade			int primary key auto_increment not null,
nome				varchar(50) not null,
fk_distribuidora    int not null,
constraint fk_distribuidora_cidade foreign key (fk_distribuidora) references distribuidora(id_distribuidora)
);

create table usuario(
id_usuario			int primary key auto_increment not null,
nome 				varchar(50) not null,
tipo_usuario		varchar(50) not null, 
telefone			varchar(15) not null,
email				varchar(50) not null,
senha				varchar(50) not null,
fk_cidade 			int,
fk_distribuidora 	int not null,
constraint fk_cidade_usuario foreign key (fk_cidade) references cidade(id_cidade),
constraint fk_distribuidora_usuario foreign key (fk_distribuidora) references distribuidora(id_distribuidora)
);


create table motivo(
id_motivo			int primary key auto_increment,
nome				varchar(50)
);

create table interrupcao(
id_interrupcao		int not null primary key,
dt_inicio			datetime not null,
dt_fim				datetime not null,
duracao_minutos INT GENERATED ALWAYS AS (TIMESTAMPDIFF(MINUTE, dt_inicio, dt_fim)) STORED,
cidade varchar (100),
motivo varchar(100)	
);

drop table interrupcao;

select * from interrupcao;

truncate interrupcao;



create table notificacao(
id_notificacao		int not null,
data_hora 			datetime not null,
tipo 				varchar(45) not null,
mensagem			varchar(45) not null,
fk_cidade			int not null,
fk_distribuidora	int not null,
constraint pk_notificacao primary key(id_notificacao, fk_cidade, fk_distribuidora),
constraint fk_cidade_notificacao foreign key (fk_cidade) references cidade(id_cidade),
constraint fk_distribuidora_cidade_notificacao foreign key (fk_distribuidora) references distribuidora(id_distribuidora)
);


create table fixacao(
id_fixacao 			int not null,
anotacao			varchar(200),
fk_notificacao 		int not null,
fk_cidade			int not null,
fk_distribuidora	int not null,
constraint pk_fixacao primary key(id_fixacao, fk_notificacao, fk_cidade, fk_distribuidora),
constraint fk_notificacao_fixacao foreign key (fk_notificacao) references notificacao(id_notificacao),
constraint fk_cidade_notificacao_fixacao foreign key (fk_cidade) references cidade(id_cidade),
constraint fk_distribuidora_cidade_interrupcao_fixacao foreign key (fk_distribuidora) references distribuidora(id_distribuidora)
);


create table log(
idLog 				int primary key auto_increment not null,
mensagem			varchar(100),
data_hora 			datetime not null,
tipo_processo		varchar(45) not null
);

create table teste(
idTeste int primary key auto_increment not null,
teste varchar(100)
);	

insert into teste values 
(null,"gay");

select * from interrupcao;

truncate interrupcao;