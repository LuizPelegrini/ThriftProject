const i32 MAX_VALUE_LEN = 1024

typedef i64 key_t
typedef i32 version_t
typedef binary value_t

struct KeyValue {
	1:key_t key,
	2:value_t value,
	3:version_t version
}

exception ValueIsTooLongException{ } 

service LameDB
{
	//Retorna null caso chave não esteja no banco.
	//Retorna o par key/value caso chave esteja no banco.
	KeyValue get(1:key_t key),

	//Retorna uma lista com todas as chaves entre keyBegin e keyEnd, inclusive.
	//Caso não existam chaves na faixa, a lista é vazia.
	list<KeyValue> getRange(1:key_t keyBegin, 2:key_t keyEnd),
	
	//Caso a chave não exista no banco, insere par com version_t = 0 e retorna 0
	//Caso a chave exista no banco, retorna a versão atual.
	version_t put(1:key_t key, 2:value_t value) throws (1:ValueIsTooLongException e)

	//Caso a chave exista no banco, sobrescreve, incrementando a versão e retorna a nova versão
	//Caso a chave não exista no banco, retorna -1 (versão inválida)
	version_t update(1:key_t key, 2:value_t value) throws (1:ValueIsTooLongException e)

	//Caso a chave exista no banco, com a chave igual ao parâmetro version,
	// sobrescreve, incrementando a versão e retorna a nova versão
	//Caso a chave não exista no banco, retorna -1 (versão inválida)
	version_t updateWithVersion(1:key_t key, 2:value_t value, 3:version_t version) throws (1:ValueIsTooLongException e)

	//Caso a chave exista no banco, remove o par e o retorna
	//Caso a chave não exista no banco, retorna null
	KeyValue remove(1:key_t key)

	//Caso a chave exista no banco, com chave igual ao parâmetro version
	//remove o par e o retorna
	//Caso a chave não exista no banco, retorna null
	KeyValue removeWithVersion(1:key_t key, 3:version_t version)
}