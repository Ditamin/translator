# Web-приложение переводчик

## Для запуска необходимо:

-установить Docker  
-в папке с проектом запустить команду docker compose up (если не работает, то сначала docker compose build) для поднятия базы данных postgres  
-запустить TranslatorApplication.java (В идеале через Intellij Idea)

После чего на порте 8081 запустится обработчик запросов, нужно отправлять POST запрос на http://localhost:8081/translation с телом вида:  
{
    "text" : "Hello",
    "translateFrom" : "en",
    "translateTo" : "ru"
} 
, где text - текст для перевода, translateFrom - язык источника, translateTo - необходимый язык перевода.
Также после запуска на http://localhost:8081/swagger-ui/index.html будет доступен swagger UI на котором можно проще тестировать запросы.

### Например с помощью curl:
curl --location 'http://localhost:8081/translation' \
--header 'Content-Type: application/json' \
--data '{
    "text" : "Hello",
    "translateFrom" : "en",
    "translateTo" : "ru"
}'

