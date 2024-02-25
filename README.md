# Exchange Rate Web Application

Учебное приложение, написанное в целях ознакомления с технологиями: 
Spring Boot, 
Spring Data,
REST API,
PostgresSQL, 
JUnit, 
Mockito.

Данное приложение предназначено для получения курса валют (к рублю) за конкретную дату.
Источниками информации о курсе являются официальный сайт центробанка России https://cbr.ru, 
сайт https://www.cbr-xml-daily.ru и тестовая база данных.

Метод `GET /exchangerate` принимает 3 параметра:
* currency - буквенный код валюты (AUD, USD и т.п.)
* date - дата в формате 'yyyy-MM-dd'
* dataSource - источник данных (опциональный параметр) 
  + json - данные с сайта https://www.cbr-xml-daily.ru (значение по умолчанию)
  + xml - данные с сайта центробанка
  + database - данные из базы данных

## Install

    maven clean install

## Run the app

    mvn spring-boot:run

# REST API

## Получение курсов валют

### Австралийский доллар за первое января 2024 с сайта центробанка

`GET /exchangerate`

    curl --location 'http://localhost:8080/exchangerate?currency=AUD&date=2024-01-01&dataSource=xml'

### Response

    61.3468