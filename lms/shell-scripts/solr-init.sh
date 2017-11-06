#!/usr/bin/env bash
solr-6.2.1/bin/solr delete -c schools
solr-6.2.1/bin/solr create -c schools
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"name","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"locale","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"permissions","type":"strings","indexed":false,"stored":true},
  "add-field":{"name":"firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdDateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"modifiedDateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"typeName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"fieldName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"fieldTypeName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"entityId","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"parentId","type":"string","indexed":false,"stored":true}
}' http://localhost:8983/solr/schools/schema


solr-6.2.1/bin/solr delete -c levels
solr-6.2.1/bin/solr create -c levels
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"name","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdDateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"modifiedDateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"typeName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"schoolId","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"fieldName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"fieldTypeName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"entityId","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"parentId","type":"string","indexed":true,"stored":true}
}' http://localhost:8983/solr/levels/schema


solr-6.2.1/bin/solr delete -c subjects
solr-6.2.1/bin/solr create -c subjects
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"name","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdDateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"modifiedDateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"typeName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"schoolId","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"fieldName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"fieldTypeName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"entityId","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"parentId","type":"string","indexed":true,"stored":true}
}' http://localhost:8983/solr/subjects/schema


solr-6.2.1/bin/solr delete -c users
solr-6.2.1/bin/solr create -c users
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
  "add-field":{"name":"createdDateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"modifiedDateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"name","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"typeName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"schoolId","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"fieldName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"fieldTypeName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"entityId","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"parentId","type":"string","indexed":true,"stored":true}
}' http://localhost:8983/solr/users/schema


solr-6.2.1/bin/solr delete -c enrollments
solr-6.2.1/bin/solr create -c enrollments
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdDateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"modifiedDateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"name","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"title","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"typeName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"fieldName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"fieldTypeName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"entityId","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"parentId","type":"string","indexed":true,"stored":true}
}' http://localhost:8983/solr/enrollments/schema


solr-6.2.1/bin/solr delete -c quizzes
solr-6.2.1/bin/solr create -c quizzes
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"title","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"instructions","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"keywords","type":"strings","indexed":false,"stored":true},
  "add-field":{"name":"firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdDateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"modifiedDateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"name","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"typeName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"schoolId","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"fieldName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"fieldTypeName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"entityId","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"parentId","type":"string","indexed":true,"stored":true}
}' http://localhost:8983/solr/quizzes/schema


solr-6.2.1/bin/solr delete -c questions
solr-6.2.1/bin/solr create -c questions
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"type","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"body","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"hint","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"explanation","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"keywords","type":"strings","indexed":false,"stored":true},
  "add-field":{"name":"feedback","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"correct","type":"boolean","indexed":false,"stored":true},
  "add-field":{"name":"score","type":"long","indexed":false,"stored":true},
  "add-field":{"name":"order","type":"int","indexed":false,"stored":true},
  "add-field":{"name":"firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdDateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"modifiedDateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"name","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"typeName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"schoolId","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"fieldName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"fieldTypeName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"entityId","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"parentId","type":"string","indexed":true,"stored":true}
}' http://localhost:8983/solr/questions/schema


solr-6.2.1/bin/solr delete -c components
solr-6.2.1/bin/solr create -c components
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"containerId","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"title","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"instructions","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"type","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"body","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"hint","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"explanation","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"feedback","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"correct","type":"boolean","indexed":false,"stored":true},
  "add-field":{"name":"score","type":"long","indexed":false,"stored":true},
  "add-field":{"name":"order","type":"int","indexed":false,"stored":true},
  "add-field":{"name":"typeName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"fieldName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"fieldTypeName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"entityId","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"resourceId","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"parentId","type":"string","indexed":true,"stored":true}
}' http://localhost:8983/solr/components/schema


solr-6.2.1/bin/solr delete -c signinlogs
solr-6.2.1/bin/solr create -c signinlogs
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"account","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"sessionId","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"ipAddress","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"dateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"fails","type":"int","indexed":true,"stored":true},
  "add-field":{"name":"typeName","type":"string","indexed":true,"stored":true}
}' http://localhost:8983/solr/signinlogs/schema