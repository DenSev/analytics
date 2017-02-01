# web-parser


What it is (What it will be): web html parser that takes page elements defined in a dedicated 'mappings' file (json/xml/groovy), creates elasticsearch mappings based on these initial mappings and puts these elements into elasticsearch index.
You will then be able to use an api based on elasticsearch query builders to query this index. 

Backed by: 
* elasticsearch-5.1.2
