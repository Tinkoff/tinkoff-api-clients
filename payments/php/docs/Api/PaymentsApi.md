# OpenAPI\Client\PaymentsApi

All URIs are relative to https://secured-openapi.business.tinkoff.ru.

Method | HTTP request | Description
------------- | ------------- | -------------
[**paymentsCoreGetStatus()**](PaymentsApi.md#paymentsCoreGetStatus) | **GET** /api/v1/payment/{paymentId} | 🔒 Получить статус платежа
[**paymentsCorePay()**](PaymentsApi.md#paymentsCorePay) | **POST** /api/v1/payment/ruble-transfer/pay | 🔒 Выполнить платёж
[**postApiV1PaymentCreate()**](PaymentsApi.md#postApiV1PaymentCreate) | **POST** /api/v1/payment/create | Создать черновик платежного поручения
[**sbpPay()**](PaymentsApi.md#sbpPay) | **POST** /api/v1/payment/sbp/pay | 🔒 Выполнить платёж через систему быстрых платежей (СБП)


## `paymentsCoreGetStatus()`

```php
paymentsCoreGetStatus($payment_id): \OpenAPI\Client\tinkoff\api\\PaymentStatusResponse
```

🔒 Получить статус платежа

Данный метод позволяет узнать статус выплаты, произведенной с помощью методов  <a href=\"#payment/PaymentsCorePay\">Выполнить платеж</a> и <a href=\"#payment/SbpPay\">Выполнить платёж через систему быстрых платежей (СБП)</a>. В поле scopes у токена должен присутствовать хотя бы один scope вида: opensme/inn/[{inn}]/kpp/[{kpp}]/payments/rub-pay, где {inn} — ИНН клиента, а {kpp} — КПП клиента. opensme/inn/[{inn}]/kpp/[{kpp}]/payments/sbp-pay, где {inn} — ИНН клиента, а {kpp} — КПП клиента.  <b>Примечание для партнёров</b>: В этом методе учитывается clientId, если ваш clientId был изменён, вы не сможете получить статус платежа, созданного при использовании старого clientId. clientId выдается при регистрации партнера в tinkoff и отправляется на почту.

### Example

```php
<?php
require_once(__DIR__ . '/vendor/autoload.php');


// Configure Bearer authorization: httpAuth
$config = OpenAPI\Client\Configuration::getDefaultConfiguration()->setAccessToken('YOUR_ACCESS_TOKEN');


$apiInstance = new OpenAPI\Client\Api\PaymentsApi(
    // If you want use custom http client, pass your client which implements `GuzzleHttp\ClientInterface`.
    // This is optional, `GuzzleHttp\Client` will be used as default.
    new GuzzleHttp\Client(),
    $config
);
$payment_id = 'payment_id_example'; // string | Идентификатор платежа

try {
    $result = $apiInstance->paymentsCoreGetStatus($payment_id);
    print_r($result);
} catch (Exception $e) {
    echo 'Exception when calling PaymentsApi->paymentsCoreGetStatus: ', $e->getMessage(), PHP_EOL;
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **payment_id** | **string**| Идентификатор платежа |

### Return type

[**\OpenAPI\Client\tinkoff\api\\PaymentStatusResponse**](../Model/PaymentStatusResponse.md)

### Authorization

[httpAuth](../../README.md#httpAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: `application/json`

[[Back to top]](#) [[Back to API list]](../../README.md#endpoints)
[[Back to Model list]](../../README.md#models)
[[Back to README]](../../README.md)

## `paymentsCorePay()`

```php
paymentsCorePay($create_payment_request)
```

🔒 Выполнить платёж

Данный метод позволяет выплачивать деньги с рублевых счетов компании. Выплаты могут производиться на счета юрлиц и физлиц в банках РФ. При передаче информации в блоке \"налоги\" появляется возможность совершать налоговые платежи и налоговые платежи за третьих лиц. Выплата денег со счета производится асинхронно. Результат запроса на выплату можно получить, вызвав метод <a href=\"#operation/PaymentsCoreGetStatus\">Получить статуса платежа</a>, передав в него соответствующий <b>paymentId</b>. В поле scope у токена должен присутствовать доступ вида  opensme/inn/[{inn}]/kpp/[{kpp}]/payments/rub-pay, где {inn} — ИНН клиента, а {kpp} — КПП клиента.  <b>Все вызовы метода дедуплицируются:</b>  <b>Селф-сервис:</b> Дедупликация происходит по paymentId в контексте вашей компании. Это значит, что paymentId должен быть уникален в рамках всех платежей от лица вашей компании. Если будет произведено два и более вызова метода с одинаковыми paymentId в рамках одной компании, то будет создан только один платеж.  <b>Партерский сценарий:</b> Дедупликация происходит по paymentId + clientId. Это значит, что связка paymentId + clientId должна быть уникальна. Если будет произведено два и более вызова метода с одинаковыми paymentId и clientId, то будет создан только один платеж. clientId выдается при регистрации партнера в tinkoff и отправляется на почту.  <b>Лимиты:</b> По умолчанию на выполнение платежей через API установлены следующие лимиты: <ul>   <li>максимальная сумма одного платежа - 100000 рублей</li>   <li>максимальная сумма платежей в день - 100000 рублей</li>   <li>максимальная сумма платежей в месяц - 1000000 рублей</li>   <li>максимальное количество платежей в день на одного контрагента - 3</li> </ul> Для изменения лимитов по вашей компании напишите на openapi@tinkoff.ru.

### Example

```php
<?php
require_once(__DIR__ . '/vendor/autoload.php');


// Configure Bearer authorization: httpAuth
$config = OpenAPI\Client\Configuration::getDefaultConfiguration()->setAccessToken('YOUR_ACCESS_TOKEN');


$apiInstance = new OpenAPI\Client\Api\PaymentsApi(
    // If you want use custom http client, pass your client which implements `GuzzleHttp\ClientInterface`.
    // This is optional, `GuzzleHttp\Client` will be used as default.
    new GuzzleHttp\Client(),
    $config
);
$create_payment_request = {"amount":10,"documentNumber":100,"dueDate":"2020-07-01T00:00+03:00","executionOrder":5,"from":{"accountNumber":"12345678900987654321"},"id":"123456","meta":{"clientCustomField":"value"},"purpose":"Оплата по договору №123. НДС не облагается","to":{"accountNumber":"11122233344455566677","bankName":"Чемпион","bik":"444555666","corrAccountNumber":"12345678901234567890","inn":"1234567890","kpp":"111222333","name":"ООО \"РОГА и КОПЫТА\""}}; // \OpenAPI\Client\tinkoff\api\\CreatePaymentRequest

try {
    $apiInstance->paymentsCorePay($create_payment_request);
} catch (Exception $e) {
    echo 'Exception when calling PaymentsApi->paymentsCorePay: ', $e->getMessage(), PHP_EOL;
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **create_payment_request** | [**\OpenAPI\Client\tinkoff\api\\CreatePaymentRequest**](../Model/CreatePaymentRequest.md)|  |

### Return type

void (empty response body)

### Authorization

[httpAuth](../../README.md#httpAuth)

### HTTP request headers

- **Content-Type**: `application/json`
- **Accept**: `application/json`

[[Back to top]](#) [[Back to API list]](../../README.md#endpoints)
[[Back to Model list]](../../README.md#models)
[[Back to README]](../../README.md)

## `postApiV1PaymentCreate()`

```php
postApiV1PaymentCreate($create_payment_draft_request): \OpenAPI\Client\tinkoff\api\\CreatePaymentDraftResponse
```

Создать черновик платежного поручения

Необходимо согласие пользователя на: \"Доступ к созданию черновиков платежных поручений\". Подробнее про поля платежного поручения и их форматы можно почитать здесь: https://glavkniga.ru/situations/k503207. В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/payments/draft/create, где {inn} — ИНН клиента, а {kpp} — КПП клиента.

### Example

```php
<?php
require_once(__DIR__ . '/vendor/autoload.php');


// Configure Bearer authorization: httpAuth
$config = OpenAPI\Client\Configuration::getDefaultConfiguration()->setAccessToken('YOUR_ACCESS_TOKEN');


$apiInstance = new OpenAPI\Client\Api\PaymentsApi(
    // If you want use custom http client, pass your client which implements `GuzzleHttp\ClientInterface`.
    // This is optional, `GuzzleHttp\Client` will be used as default.
    new GuzzleHttp\Client(),
    $config
);
$create_payment_draft_request = {"accountNumber":"40802810501234567890","amount":1.0,"bankAcnt":"03100643000000015100","bankBik":1.500495E7,"date":"2021-12-30T00:00+03:00[Europe/Moscow]","documentNumber":"514462","inn":"7710140679","kbk":"18210301000011000110","kpp":"771301001","oktmo":"50701000","paymentPurpose":"Налоги на прибыль","recipientCorrAccountNumber":"40102810445370000043","recipientName":"СИБИРСКОЕ ГУ БАНКА РОССИИ//УФК по Новосибирской области г. Новосибирск","taxDocDate":"10.01.2020","taxDocNumber":"1","taxEvidence":"ТП","taxPayerStatus":"01","taxPeriod":"ГД.00.2021","uin":"12345678912345678900"}; // \OpenAPI\Client\tinkoff\api\\CreatePaymentDraftRequest

try {
    $result = $apiInstance->postApiV1PaymentCreate($create_payment_draft_request);
    print_r($result);
} catch (Exception $e) {
    echo 'Exception when calling PaymentsApi->postApiV1PaymentCreate: ', $e->getMessage(), PHP_EOL;
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **create_payment_draft_request** | [**\OpenAPI\Client\tinkoff\api\\CreatePaymentDraftRequest**](../Model/CreatePaymentDraftRequest.md)|  |

### Return type

[**\OpenAPI\Client\tinkoff\api\\CreatePaymentDraftResponse**](../Model/CreatePaymentDraftResponse.md)

### Authorization

[httpAuth](../../README.md#httpAuth)

### HTTP request headers

- **Content-Type**: `application/json`
- **Accept**: `application/json`

[[Back to top]](#) [[Back to API list]](../../README.md#endpoints)
[[Back to Model list]](../../README.md#models)
[[Back to README]](../../README.md)

## `sbpPay()`

```php
sbpPay($create_sbp_payment_request)
```

🔒 Выполнить платёж через систему быстрых платежей (СБП)

Данный метод позволяет производить выплаты с рублевых счетов компании через систему быстрых платежей (СБП). Выплаты производятся по номеру телефона на счета физлиц в банках РФ, подключенных к системе быстрых платежей. Выплата денег со счета производится асинхронно. Результат запроса на выплату можно получить, вызвав метод <a href=\"#operation/PaymentsCoreGetStatus\">Получить статуса платежа</a>, передав в него соответствующий <b>paymentId</b>. В поле scope у токена должен присутствовать доступ вида  opensme/inn/[{inn}]/kpp/[{kpp}]/payments/sbp-pay, где {inn} — ИНН клиента, а {kpp} — КПП клиента.  Для совершения выплаты необходимо указать идентификатор банка {bankId} в системе СБП.  <b>Список банков, поддерживающих переводы физлицам через систему СБП:</b>  Наименование | bankId ----------------------|------------- Тинькофф Банк | 100000000004 ВТБ | 100000000005 ЮМани | 100000000022 Банк АВАНГАРД | 100000000028 Газэнергобанк | 100000000043 Экспобанк | 100000000044 КБ ПЛАТИНА | 100000000048 Банк ВБРР | 100000000049 ТОЧКА (ФК ОТКРЫТИЕ) | 100000000065 АБ РОССИЯ | 100000000095 КБ Модульбанк | 100000000099 Русское финансовое общество | 100000000104 МСП Банк | 100000000246  <b>Все вызовы метода дедуплицируются:</b>  <b>Селф-сервис:</b> Дедупликация происходит по paymentId в контексте вашей компании. Это значит, что paymentId должен быть уникален в рамках всех платежей от лица вашей компании. Если будет произведено два и более вызова метода с одинаковыми paymentId в рамках одной компании, то будет создан только один платеж.  <b>Партерский сценарий:</b> Дедупликация происходит по paymentId + clientId. Это значит, что связка paymentId + clientId должна быть уникальна. Если будет произведено два и более вызова метода с одинаковыми paymentId и clientId, то будет создан только один платеж. clientId выдается при регистрации партнера в tinkoff и отправляется на почту.  <b>Лимиты:</b> По умолчанию на выполнение платежей через API установлены следующие лимиты: <ul>   <li>максимальная сумма одного платежа - 100000 рублей</li>   <li>максимальная сумма платежей в день - 100000 рублей</li>   <li>максимальная сумма платежей в месяц - 1000000 рублей</li>   <li>максимальное количество платежей в день на одного контрагента - 3</li> </ul> Для изменения лимитов по вашей компании напишите на openapi@tinkoff.ru.

### Example

```php
<?php
require_once(__DIR__ . '/vendor/autoload.php');


// Configure Bearer authorization: httpAuth
$config = OpenAPI\Client\Configuration::getDefaultConfiguration()->setAccessToken('YOUR_ACCESS_TOKEN');


$apiInstance = new OpenAPI\Client\Api\PaymentsApi(
    // If you want use custom http client, pass your client which implements `GuzzleHttp\ClientInterface`.
    // This is optional, `GuzzleHttp\Client` will be used as default.
    new GuzzleHttp\Client(),
    $config
);
$create_sbp_payment_request = {"amount":10,"documentNumber":100,"from":{"accountNumber":"12345678900987654321"},"id":"123456","purpose":"Оплата по договору №123. НДС не облагается","to":{"bankId":"100000000004","firstName":"Иван","lastName":"Иванов","middleName":"Иванович","phone":"+72561112233"}}; // \OpenAPI\Client\tinkoff\api\\CreateSbpPaymentRequest

try {
    $apiInstance->sbpPay($create_sbp_payment_request);
} catch (Exception $e) {
    echo 'Exception when calling PaymentsApi->sbpPay: ', $e->getMessage(), PHP_EOL;
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **create_sbp_payment_request** | [**\OpenAPI\Client\tinkoff\api\\CreateSbpPaymentRequest**](../Model/CreateSbpPaymentRequest.md)|  |

### Return type

void (empty response body)

### Authorization

[httpAuth](../../README.md#httpAuth)

### HTTP request headers

- **Content-Type**: `application/json`
- **Accept**: `application/json`

[[Back to top]](#) [[Back to API list]](../../README.md#endpoints)
[[Back to Model list]](../../README.md#models)
[[Back to README]](../../README.md)
