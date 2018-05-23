#!/usr/bin/env bash
solr-7.2.1/bin/solr delete -c lms.schools
solr-7.2.1/bin/solr create -c lms.schools
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"name","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdBy.id","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"createdBy.firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdBy.lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdOn","type":"pdate","indexed":true,"stored":true},
  "add-field":{"name":"modifiedBy.id","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"modifiedBy.firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"modifiedBy.lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"modifiedOn","type":"pdate","indexed":true,"stored":true}
}' http://localhost:8983/solr/lms.schools/schema


solr-7.2.1/bin/solr delete -c lms.levels
solr-7.2.1/bin/solr create -c lms.levels
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"name","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"school.id","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"school.name","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"createdBy.id","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"createdBy.firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdBy.lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdOn","type":"pdate","indexed":true,"stored":true},
  "add-field":{"name":"modifiedBy.id","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"modifiedBy.firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"modifiedBy.lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"modifiedOn","type":"pdate","indexed":true,"stored":true}
}' http://localhost:8983/solr/lms.levels/schema


solr-7.2.1/bin/solr delete -c lms.subjects
solr-7.2.1/bin/solr create -c lms.subjects
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"name","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"school.id","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"school.name","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"createdBy.id","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"createdBy.firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdBy.lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdOn","type":"pdate","indexed":true,"stored":true},
  "add-field":{"name":"modifiedBy.id","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"modifiedBy.firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"modifiedBy.lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"modifiedOn","type":"pdate","indexed":true,"stored":true}
}' http://localhost:8983/solr/lms.subjects/schema


solr-7.2.1/bin/solr delete -c lms.groups
solr-7.2.1/bin/solr create -c lms.groups
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"name","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"school.id","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"school.name","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"createdBy.id","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"createdBy.firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdBy.lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdOn","type":"pdate","indexed":true,"stored":true},
  "add-field":{"name":"modifiedBy.id","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"modifiedBy.firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"modifiedBy.lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"modifiedOn","type":"pdate","indexed":true,"stored":true}
}' http://localhost:8983/solr/lms.groups/schema


solr-7.2.1/bin/solr delete -c lms.users
solr-7.2.1/bin/solr create -c lms.users
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"dateOfBirth","type":"pdate","indexed":true,"stored":true},
  "add-field":{"name":"gender","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"mobile","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"email","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"account","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"school.id","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"school.name","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"createdBy.id","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"createdBy.firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdBy.lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdOn","type":"pdate","indexed":true,"stored":true},
  "add-field":{"name":"modifiedBy.id","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"modifiedBy.firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"modifiedBy.lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"modifiedOn","type":"pdate","indexed":true,"stored":true}
}' http://localhost:8983/solr/lms.users/schema


solr-7.2.1/bin/solr delete -c lms.subscriptions
solr-7.2.1/bin/solr create -c lms.subscriptions
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"title","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"description","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"subscribedDateTime","type":"pdate","indexed":true,"stored":true},
  "add-field":{"name":"typeName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"fieldName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"fieldTypeName","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"entityId","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"parentId","type":"string","indexed":true,"stored":true}
}' http://localhost:8983/solr/lms.subscriptions/schema


solr-7.2.1/bin/solr delete -c lms.courses
solr-7.2.1/bin/solr create -c lms.courses
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"title","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"description","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"keywords","type":"strings","indexed":true,"stored":true},
  "add-field":{"name":"school.id","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"school.name","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"createdBy.id","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"createdBy.firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdBy.lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdOn","type":"pdate","indexed":true,"stored":true},
  "add-field":{"name":"modifiedBy.id","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"modifiedBy.firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"modifiedBy.lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"modifiedOn","type":"pdate","indexed":true,"stored":true}
}' http://localhost:8983/solr/lms.courses/schema


solr-7.2.1/bin/solr delete -c lms.lessons
solr-7.2.1/bin/solr create -c lms.lessons
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"title","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"description","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"keywords","type":"strings","indexed":true,"stored":true},
  "add-field":{"name":"school.id","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"school.name","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"createdBy.id","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"createdBy.firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdBy.lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdOn","type":"pdate","indexed":true,"stored":true},
  "add-field":{"name":"modifiedBy.id","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"modifiedBy.firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"modifiedBy.lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"modifiedOn","type":"pdate","indexed":true,"stored":true}
}' http://localhost:8983/solr/lms.lessons/schema


solr-7.2.1/bin/solr delete -c lms.quizzes
solr-7.2.1/bin/solr create -c lms.quizzes
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"title","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"description","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"keywords","type":"strings","indexed":true,"stored":true},
  "add-field":{"name":"school.id","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"school.name","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"createdBy.id","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"createdBy.firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdBy.lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdOn","type":"pdate","indexed":true,"stored":true},
  "add-field":{"name":"modifiedBy.id","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"modifiedBy.firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"modifiedBy.lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"modifiedOn","type":"pdate","indexed":true,"stored":true}
}' http://localhost:8983/solr/lms.quizzes/schema


solr-7.2.1/bin/solr delete -c lms.components
solr-7.2.1/bin/solr create -c lms.components
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"containerId","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"title","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"description","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"instructions","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"type","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"body","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"correct","type":"boolean","indexed":false,"stored":true},
  "add-field":{"name":"score","type":"plong","indexed":false,"stored":true},
  "add-field":{"name":"content","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"lessonId","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"quizId","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"resourceId","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"entityId","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"parentId","type":"string","indexed":true,"stored":true}
}' http://localhost:8983/solr/lms.components/schema


solr-7.2.1/bin/solr delete -c lms.questions
solr-7.2.1/bin/solr create -c lms.questions
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"type","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"body","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"keywords","type":"strings","indexed":true,"stored":true},
  "add-field":{"name":"correct","type":"boolean","indexed":false,"stored":true},
  "add-field":{"name":"score","type":"plong","indexed":false,"stored":true},
  "add-field":{"name":"school.id","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"school.name","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"createdBy.id","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"createdBy.firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdBy.lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdOn","type":"pdate","indexed":true,"stored":true},
  "add-field":{"name":"modifiedBy.id","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"modifiedBy.firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"modifiedBy.lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"modifiedOn","type":"pdate","indexed":true,"stored":true}
}' http://localhost:8983/solr/lms.questions/schema


solr-7.2.1/bin/solr delete -c lms.files
solr-7.2.1/bin/solr create -c lms.files
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"name","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"size","type":"pint","indexed":false,"stored":true},
  "add-field":{"name":"directory","type":"boolean","indexed":false,"stored":true},
  "add-field":{"name":"school.id","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"school.name","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"createdBy.id","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"createdBy.firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdBy.lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"createdOn","type":"pdate","indexed":true,"stored":true},
  "add-field":{"name":"modifiedBy.id","type":"string","indexed":false,"stored":true},
  "add-field":{"name":"modifiedBy.firstName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"modifiedBy.lastName","type":"text_general","indexed":true,"stored":true,"multiValued":false},
  "add-field":{"name":"modifiedOn","type":"pdate","indexed":true,"stored":true}
}' http://localhost:8983/solr/lms.files/schema