#!/usr/bin/env bash
bin/solr delete -c schools
bin/solr create -c schools
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"name","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"locale","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"permissions","type":"strings","indexed":false,"stored":true},
  "add-field":{"name":"firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdDateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"modifiedDateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"fieldName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"parentId","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"typeName","type":"string","indexed":true,"stored":true}
}' http://localhost:8983/solr/schools/schema

bin/solr delete -c levels
bin/solr create -c levels
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"name","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdDateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"modifiedDateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"fieldName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"parentId","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"typeName","type":"string","indexed":true,"stored":true}
}' http://localhost:8983/solr/levels/schema

bin/solr delete -c subjects
bin/solr create -c subjects
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"name","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdDateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"modifiedDateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"fieldName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"parentId","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"typeName","type":"string","indexed":true,"stored":true}
}' http://localhost:8983/solr/subjects/schema

bin/solr delete -c users
bin/solr create -c users
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"account","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"password","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"salt","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"dateOfBirth","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"gender","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"mobile","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"email","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"locale","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"dateFormat","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"dateTimeFormat","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"permissions","type":"strings","indexed":false,"stored":true},
  "add-field":{"name":"name","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdDateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"modifiedDateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"fieldName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"parentId","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"typeName","type":"string","indexed":true,"stored":true}
}' http://localhost:8983/solr/users/schema

bin/solr delete -c questions
bin/solr create -c questions
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"type","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"body","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"hint","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"explanation","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdDateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"modifiedDateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"feedback","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"correct","type":"boolean","indexed":false,"stored":true},
  "add-field":{"name":"order","type":"int","indexed":false,"stored":true},
  "add-field":{"name":"name","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"fieldName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"parentId","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"typeName","type":"string","indexed":true,"stored":true}
}' http://localhost:8983/solr/questions/schema

bin/solr delete -c quizzes
bin/solr create -c quizzes
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"title","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"instructions","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"type","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"body","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"hint","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"explanation","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdDateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"modifiedDateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"feedback","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"correct","type":"boolean","indexed":false,"stored":true},
  "add-field":{"name":"section","type":"int","indexed":false,"stored":true},
  "add-field":{"name":"order","type":"int","indexed":false,"stored":true},
  "add-field":{"name":"name","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"fieldName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"parentId","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"typeName","type":"string","indexed":true,"stored":true}
}' http://localhost:8983/solr/quizzes/schema