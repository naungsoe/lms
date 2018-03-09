#!/usr/bin/env bash
solr-6.2.1/bin/solr delete -c lms_schools
solr-6.2.1/bin/solr create -c lms_schools
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
}' http://localhost:8983/solr/lms_schools/schema


solr-6.2.1/bin/solr delete -c lms_levels
solr-6.2.1/bin/solr create -c lms_levels
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
}' http://localhost:8983/solr/lms_levels/schema


solr-6.2.1/bin/solr delete -c lms_subjects
solr-6.2.1/bin/solr create -c lms_subjects
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
}' http://localhost:8983/solr/lms_subjects/schema


solr-6.2.1/bin/solr delete -c lms_groups
solr-6.2.1/bin/solr create -c lms_groups
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
}' http://localhost:8983/solr/lms_groups/schema


solr-6.2.1/bin/solr delete -c lms_users
solr-6.2.1/bin/solr create -c lms_users
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
}' http://localhost:8983/solr/lms_users/schema


solr-6.2.1/bin/solr delete -c lms_subscriptions
solr-6.2.1/bin/solr create -c lms_subscriptions
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
}' http://localhost:8983/solr/lms_subscriptions/schema


solr-6.2.1/bin/solr delete -c lms_courses
solr-6.2.1/bin/solr create -c lms_courses
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
}' http://localhost:8983/solr/lms_courses/schema


solr-6.2.1/bin/solr delete -c lms_lessons
solr-6.2.1/bin/solr create -c lms_lessons
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
}' http://localhost:8983/solr/lms_lessons/schema


solr-6.2.1/bin/solr delete -c lms_quizzes
solr-6.2.1/bin/solr create -c lms_quizzes
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
}' http://localhost:8983/solr/lms_quizzes/schema


solr-6.2.1/bin/solr delete -c lms_components
solr-6.2.1/bin/solr create -c lms_components
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
}' http://localhost:8983/solr/lms_components/schema


solr-6.2.1/bin/solr delete -c lms_questions
solr-6.2.1/bin/solr create -c lms_questions
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
}' http://localhost:8983/solr/lms_questions/schema


solr-6.2.1/bin/solr delete -c lms_files
solr-6.2.1/bin/solr create -c lms_files
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
}' http://localhost:8983/solr/lms_files/schema


solr-6.2.1/bin/solr delete -c lms_signinlogs
solr-6.2.1/bin/solr create -c lms_signinlogs
curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{"name":"account","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"sessionId","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"ipAddress","type":"string","indexed":true,"stored":true},
  "add-field":{"name":"dateTime","type":"date","indexed":true,"stored":true},
  "add-field":{"name":"fails","type":"int","indexed":true,"stored":true},
  "add-field":{"name":"typeName","type":"string","indexed":true,"stored":true}
}' http://localhost:8983/solr/lms_signinlogs/schema