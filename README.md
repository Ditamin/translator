# Web-приложение переводчик

## Для запуска необходимо:

-установить Docker  
-в папке с проектом запустить команду docker compose up (если не работает, то сначала docker compose build)

После чего на порте 8181 запустится обработчик запросов, нужно отправлять POST запрос на http://localhost:8181/translation с телом вида:  
{
    "text" : "Hello",
    "translateFrom" : "en",
    "translateTo" : "ru"
} 
, где text - текст для перевода, translateFrom - язык источника, translateTo - необходимый язык перевода.

### Например с помощью curl:
curl --location 'http://localhost:8181/translation' \
--header 'Content-Type: application/json' \
--data '{
    "text" : "Hello",
    "translateFrom" : "en",
    "translateTo" : "ru"
}'

### Внимание
Поднятый через docker java класс не хочет подсоединяться к postgres контейнеру, поэтому он не может записывать данные. Для корректной работы необходимо также поднять docker compose и отдельно запустить TranslatorApplication (В идеале через Intellij Idea) и тогда можно спокойно отправлять запросы, но уже на 8081 порт (http://localhost:8081/translation)

