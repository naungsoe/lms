#!/usr/bin/env bash
solr-6.2.1/bin/solr delete -c lms.schools
solr-6.2.1/bin/solr create -c lms.schools
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
}' http://localhost:8983/solr/lms.schools/schema


solr-6.2.1/bin/solr delete -c lms.levels
solr-6.2.1/bin/solr create -c lms.levels
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
}' http://localhost:8983/solr/lms.levels/schema


solr-6.2.1/bin/solr delete -c lms.subjects
solr-6.2.1/bin/solr create -c lms.subjects
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
}' http://localhost:8983/solr/lms.subjects/schema


solr-6.2.1/bin/solr delete -c lms.groups
solr-6.2.1/bin/solr create -c lms.groups
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"name","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"permissions","type":"strings","indexed":false,"stored":true},
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
}' http://localhost:8983/solr/lms.groups/schema


solr-6.2.1/bin/solr delete -c lms.users
solr-6.2.1/bin/solr create -c lms.users
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"account","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"password","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"salt","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"dateOfBirth","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"gender","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"mobile","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"email","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"locale","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"timeFormat","type":"string","indexed":false,"stored":true},
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
}' http://localhost:8983/solr/lms.users/schema


solr-6.2.1/bin/solr delete -c lms.subscriptions
solr-6.2.1/bin/solr create -c lms.subscriptions
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"title","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"description","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"subscribedDateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"typeName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"fieldName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"fieldTypeName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"entityId","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"parentId","type":"string","indexed":true,"stored":true}
}' http://localhost:8983/solr/lms.subscriptions/schema


solr-6.2.1/bin/solr delete -c lms.courses
solr-6.2.1/bin/solr create -c lms.courses
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"title","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"description","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"keywords","type":"strings","indexed":true,"stored":true},
  "add-field":{"name":"status","type":"string","indexed":true,"stored":true},
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
}' http://localhost:8983/solr/lms.courses/schema


solr-6.2.1/bin/solr delete -c lms.lessons
solr-6.2.1/bin/solr create -c lms.lessons
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"title","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"description","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"keywords","type":"strings","indexed":true,"stored":true},
  "add-field":{"name":"status","type":"string","indexed":true,"stored":true},
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
}' http://localhost:8983/solr/lms.lessons/schema


solr-6.2.1/bin/solr delete -c lms.quizzes
solr-6.2.1/bin/solr create -c lms.quizzes
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"title","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"description","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"keywords","type":"strings","indexed":true,"stored":true},
  "add-field":{"name":"status","type":"string","indexed":true,"stored":true},
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
}' http://localhost:8983/solr/lms.quizzes/schema


solr-6.2.1/bin/solr delete -c lms.components
solr-6.2.1/bin/solr create -c lms.components
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"containerId","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"title","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"description","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"instructions","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"type","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"body","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"hint","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"explanation","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"feedback","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"correct","type":"boolean","indexed":false,"stored":true},
  "add-field":{"name":"score","type":"long","indexed":false,"stored":true},
  "add-field":{"name":"order","type":"int","indexed":false,"stored":true},
  "add-field":{"name":"content","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"typeName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"fieldName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"fieldTypeName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"lessonId","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"quizId","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"resourceId","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"entityId","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"parentId","type":"string","indexed":true,"stored":true}
}' http://localhost:8983/solr/lms.components/schema


solr-6.2.1/bin/solr delete -c lms.questions
solr-6.2.1/bin/solr create -c lms.questions
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"type","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"body","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"hint","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"explanation","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"keywords","type":"strings","indexed":true,"stored":true},
  "add-field":{"name":"status","type":"string","indexed":true,"stored":true},
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
}' http://localhost:8983/solr/lms.questions/schema


solr-6.2.1/bin/solr delete -c lms.files
solr-6.2.1/bin/solr create -c lms.files
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"name","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"size","type":"int","indexed":false,"stored":true},
  "add-field":{"name":"directory","type":"boolean","indexed":false,"stored":true},
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
}' http://localhost:8983/solr/lms.files/schema


solr-6.2.1/bin/solr delete -c lms.signinlogs
solr-6.2.1/bin/solr create -c lms.signinlogs
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"account","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"sessionId","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"ipAddress","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"dateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"fails","type":"int","indexed":true,"stored":true},
  "add-field":{"name":"typeName","type":"string","indexed":true,"stored":true}
}' http://localhost:8983/solr/lms.signinlogs/schema