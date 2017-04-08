#!/usr/bin/env bash
bin/solr delete -c users
bin/solr create -c users
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"account","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"password","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"salt","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"firstName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"lastName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"dateOfBirth","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"gender","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"mobile","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"email","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"locale","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"dateFormat","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"dateTimeFormat","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"permissions","type":"strings","indexed":false,"stored":true},
  "add-field":{"name":"name","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"createdDateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"modifiedDateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"fieldName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"typeName","type":"string","indexed":true,"stored":true}
}' http://localhost:8983/solr/users/schema


bin/solr delete -c questions
bin/solr create -c questions
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"type","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"body","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"hint","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"explanation","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"firstName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"lastName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"createdDateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"modifiedDateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"feedback","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"correct","type":"boolean","indexed":false,"stored":true},
  "add-field":{"name":"order","type":"int","indexed":false,"stored":true},
  "add-field":{"name":"fieldName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"typeName","type":"string","indexed":true,"stored":true}
}' http://localhost:8983/solr/questions/schema