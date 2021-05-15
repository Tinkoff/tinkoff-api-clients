# OpenAPI\Client\SalaryApi

All URIs are relative to https://secured-openapi.business.tinkoff.ru.

Method | HTTP request | Description
------------- | ------------- | -------------
[**salaryCreateEmployee()**](SalaryApi.md#salaryCreateEmployee) | **POST** /api/v1/salary/employees/create | Создать черновики анкет сотрудников
[**salaryCreatePaymentRegistry()**](SalaryApi.md#salaryCreatePaymentRegistry) | **POST** /api/v1/salary/payment-registry/create | Создание черновика платежного реестра
[**salaryGetEmployeesCreateResult()**](SalaryApi.md#salaryGetEmployeesCreateResult) | **GET** /api/v1/salary/employees/create/result | Получить результат создания черновиков анкет сотрудников
[**salaryGetEmployeesList()**](SalaryApi.md#salaryGetEmployeesList) | **POST** /api/v1/salary/employees/list | Получить информацию по сотрудникам
[**salaryGetPaymentRegistry()**](SalaryApi.md#salaryGetPaymentRegistry) | **GET** /api/v1/salary/payment-registry/{paymentRegistryId} | Получение платежного реестра
[**salaryGetPaymentRegistryCreateResult()**](SalaryApi.md#salaryGetPaymentRegistryCreateResult) | **GET** /api/v1/salary/payment-registry/create/result | Получить результат создания черновика платежного реестра
[**salaryPaymentRegistrySubmit()**](SalaryApi.md#salaryPaymentRegistrySubmit) | **POST** /api/v1/salary/payment-registry/submit | 🔒 Подписание платёжного реестра сотрудников
[**salaryPaymentRegistrySubmitResult()**](SalaryApi.md#salaryPaymentRegistrySubmitResult) | **GET** /api/v1/salary/payment-registry/submit/result | 🔒 Получить результат подписания платёжного реестра сотрудников


## `salaryCreateEmployee()`

```php
salaryCreateEmployee($create_employees_request): \OpenAPI\Client\tinkoff\api\\CreateEmployeesResponse
```

Создать черновики анкет сотрудников

Запрос является асинхронной операцией — его результат можно получить, вызвав метод <a href=\"#operation/SalaryGetEmployeesCreateResult\">Получить результат создания сотрудников</a>, передав в него соответствующий <b>correlationId</b>. В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/employees/manage, где {inn} — ИНН клиента, а {kpp} — КПП клиента.

### Example

```php
<?php
require_once(__DIR__ . '/vendor/autoload.php');


// Configure Bearer authorization: httpAuth
$config = OpenAPI\Client\Configuration::getDefaultConfiguration()->setAccessToken('YOUR_ACCESS_TOKEN');


$apiInstance = new OpenAPI\Client\Api\SalaryApi(
    // If you want use custom http client, pass your client which implements `GuzzleHttp\ClientInterface`.
    // This is optional, `GuzzleHttp\Client` will be used as default.
    new GuzzleHttp\Client(),
    $config
);
$create_employees_request = {"correlationId":"cf99df08-0829-4614-8da3-0e440fd23fe0","employees":[{"addresses":[{"apartment":"512","building":"1","city":"Санкт-Петербург","construction":"1","country":"Россия","district":"Санкт-Петербург","house":"12","postalCode":"123123","settlement":"Санкт-Петербург","state":"Санкт-Петербург","street":"ул. Херсонская","type":"Работы"},{"apartment":"20","building":"8","city":"Санкт-Петербург","construction":"16","country":"Россия","district":"Санкт-Петербург","house":"4","postalCode":"123123","settlement":"Санкт-Петербург","state":"Санкт-Петербург","street":"ул. Таллинская","type":"Жительства"},{"apartment":"20","building":"8","city":"Санкт-Петербург","construction":"16","country":"РОССИЯ","district":"Санкт-Петербург","house":"4","postalCode":"123123","settlement":"Санкт-Петербург","state":"Санкт-Петербург","street":"ул. Таллинская","type":"Жительства"}],"birthDate":"1967-12-25","birthPlace":"Санкт-Петербург","citizenship":"Санкт-Петербург","documents":[{"date":"2015-05-09","division":"123-123","expireDate":"2025-05-09","number":"123456","organization":"ФМС","serial":"1234","type":"Паспорт"}],"email":"example@example.com","firstName":"Иван","jobInfo":{"position":"программист"},"lastName":"Иванов","latinFirstName":"Ivan","latinLastName":"Ivanov","middleName":"Иванович","number":1,"phones":[{"number":"+72565121024","type":"Мобильный"}]}]}; // \OpenAPI\Client\tinkoff\api\\CreateEmployeesRequest

try {
    $result = $apiInstance->salaryCreateEmployee($create_employees_request);
    print_r($result);
} catch (Exception $e) {
    echo 'Exception when calling SalaryApi->salaryCreateEmployee: ', $e->getMessage(), PHP_EOL;
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **create_employees_request** | [**\OpenAPI\Client\tinkoff\api\\CreateEmployeesRequest**](../Model/CreateEmployeesRequest.md)|  |

### Return type

[**\OpenAPI\Client\tinkoff\api\\CreateEmployeesResponse**](../Model/CreateEmployeesResponse.md)

### Authorization

[httpAuth](../../README.md#httpAuth)

### HTTP request headers

- **Content-Type**: `application/json`
- **Accept**: `application/json`

[[Back to top]](#) [[Back to API list]](../../README.md#endpoints)
[[Back to Model list]](../../README.md#models)
[[Back to README]](../../README.md)

## `salaryCreatePaymentRegistry()`

```php
salaryCreatePaymentRegistry($create_payment_registry_request): \OpenAPI\Client\tinkoff\api\\CreatePaymentRegistryResponse
```

Создание черновика платежного реестра

Запрос является асинхронной операцией — его результат можно получить, вызвав метод <a href=\"#operation/SalaryGetPaymentRegistryCreateResult\">Получить результат создания платежного реестра</a>, передав в него соответствующий <b>correlationId</b>.<br> <u>Вы можете добавлять в реестр сотрудников, которые находятся в статусах ACTIVE и FIRED</u><br> В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/payment-registry/manage, где {inn} — ИНН клиента, а {kpp} — КПП клиента.

### Example

```php
<?php
require_once(__DIR__ . '/vendor/autoload.php');


// Configure Bearer authorization: httpAuth
$config = OpenAPI\Client\Configuration::getDefaultConfiguration()->setAccessToken('YOUR_ACCESS_TOKEN');


$apiInstance = new OpenAPI\Client\Api\SalaryApi(
    // If you want use custom http client, pass your client which implements `GuzzleHttp\ClientInterface`.
    // This is optional, `GuzzleHttp\Client` will be used as default.
    new GuzzleHttp\Client(),
    $config
);
$create_payment_registry_request = {"companyAccountNumber":"40802123456789012345","correlationId":"cf99df08-0829-4614-8da3-0e440fd23fe0","loadDate":"2015-05-09T12:30","payments":[{"accountNumber":"12345678901234567890","collectionAmount":50,"employeeInfo":{"firstName":"Петр","lastName":"Петров","middleName":"Петрович"},"number":1,"paymentPurpose":"Зарплата","periodEnd":"2016-05-09","periodStart":"2015-05-09","revenueTypeCode":"2","sum":65000}],"registryCreateType":"FAIL_ERRORS"}; // \OpenAPI\Client\tinkoff\api\\CreatePaymentRegistryRequest

try {
    $result = $apiInstance->salaryCreatePaymentRegistry($create_payment_registry_request);
    print_r($result);
} catch (Exception $e) {
    echo 'Exception when calling SalaryApi->salaryCreatePaymentRegistry: ', $e->getMessage(), PHP_EOL;
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **create_payment_registry_request** | [**\OpenAPI\Client\tinkoff\api\\CreatePaymentRegistryRequest**](../Model/CreatePaymentRegistryRequest.md)|  |

### Return type

[**\OpenAPI\Client\tinkoff\api\\CreatePaymentRegistryResponse**](../Model/CreatePaymentRegistryResponse.md)

### Authorization

[httpAuth](../../README.md#httpAuth)

### HTTP request headers

- **Content-Type**: `application/json`
- **Accept**: `application/json`

[[Back to top]](#) [[Back to API list]](../../README.md#endpoints)
[[Back to Model list]](../../README.md#models)
[[Back to README]](../../README.md)

## `salaryGetEmployeesCreateResult()`

```php
salaryGetEmployeesCreateResult($correlation_id): \OpenAPI\Client\tinkoff\api\\CreateEmployeeResultResponse
```

Получить результат создания черновиков анкет сотрудников

Метод возвращает результат запроса на <a href=\"#operation/SalaryCreateEmployee\">создание черновиков анкет сотрудников</a>. Ответ на запрос создания хранится в течение 2-х дней.<br> В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/employees/manage, где {inn} — ИНН клиента, а {kpp} — КПП клиента.

### Example

```php
<?php
require_once(__DIR__ . '/vendor/autoload.php');


// Configure Bearer authorization: httpAuth
$config = OpenAPI\Client\Configuration::getDefaultConfiguration()->setAccessToken('YOUR_ACCESS_TOKEN');


$apiInstance = new OpenAPI\Client\Api\SalaryApi(
    // If you want use custom http client, pass your client which implements `GuzzleHttp\ClientInterface`.
    // This is optional, `GuzzleHttp\Client` will be used as default.
    new GuzzleHttp\Client(),
    $config
);
$correlation_id = cf99df08-0829-4614-8da3-0e440fd23fe0; // string | Уникальный идентификатор(тип <a href=\"https://en.wikipedia.org/wiki/Universally_unique_identifier\">UUID</a>), связывающий запрос создания с запросом получения ответа (должен быть сформирован на стороне клиента)

try {
    $result = $apiInstance->salaryGetEmployeesCreateResult($correlation_id);
    print_r($result);
} catch (Exception $e) {
    echo 'Exception when calling SalaryApi->salaryGetEmployeesCreateResult: ', $e->getMessage(), PHP_EOL;
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **correlation_id** | **string**| Уникальный идентификатор(тип &lt;a href&#x3D;\&quot;https://en.wikipedia.org/wiki/Universally_unique_identifier\&quot;&gt;UUID&lt;/a&gt;), связывающий запрос создания с запросом получения ответа (должен быть сформирован на стороне клиента) |

### Return type

[**\OpenAPI\Client\tinkoff\api\\CreateEmployeeResultResponse**](../Model/CreateEmployeeResultResponse.md)

### Authorization

[httpAuth](../../README.md#httpAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: `application/json`

[[Back to top]](#) [[Back to API list]](../../README.md#endpoints)
[[Back to Model list]](../../README.md#models)
[[Back to README]](../../README.md)

## `salaryGetEmployeesList()`

```php
salaryGetEmployeesList($employees_info_request): \OpenAPI\Client\tinkoff\api\\EmployeeResponse
```

Получить информацию по сотрудникам

Метод вызывается для получения информации по сотрудникам. Вызывать необходимо не чаще, чем раз в 10 минут.<br> Заявка для добавления сотрудника создается в статусе DRAFT, после чего ее необходимо отправить в Личном кабинете. Отправленный сотрудник перейдет в статус ACTIVE.<br> В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/employees/manage, где {inn} — ИНН клиента, а {kpp} — КПП клиента.

### Example

```php
<?php
require_once(__DIR__ . '/vendor/autoload.php');


// Configure Bearer authorization: httpAuth
$config = OpenAPI\Client\Configuration::getDefaultConfiguration()->setAccessToken('YOUR_ACCESS_TOKEN');


$apiInstance = new OpenAPI\Client\Api\SalaryApi(
    // If you want use custom http client, pass your client which implements `GuzzleHttp\ClientInterface`.
    // This is optional, `GuzzleHttp\Client` will be used as default.
    new GuzzleHttp\Client(),
    $config
);
$employees_info_request = {"employeeIds":[217]}; // \OpenAPI\Client\tinkoff\api\\EmployeesInfoRequest | Список идентификаторов сотрудников

try {
    $result = $apiInstance->salaryGetEmployeesList($employees_info_request);
    print_r($result);
} catch (Exception $e) {
    echo 'Exception when calling SalaryApi->salaryGetEmployeesList: ', $e->getMessage(), PHP_EOL;
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **employees_info_request** | [**\OpenAPI\Client\tinkoff\api\\EmployeesInfoRequest**](../Model/EmployeesInfoRequest.md)| Список идентификаторов сотрудников |

### Return type

[**\OpenAPI\Client\tinkoff\api\\EmployeeResponse**](../Model/EmployeeResponse.md)

### Authorization

[httpAuth](../../README.md#httpAuth)

### HTTP request headers

- **Content-Type**: `application/json`
- **Accept**: `application/json`

[[Back to top]](#) [[Back to API list]](../../README.md#endpoints)
[[Back to Model list]](../../README.md#models)
[[Back to README]](../../README.md)

## `salaryGetPaymentRegistry()`

```php
salaryGetPaymentRegistry($payment_registry_id): \OpenAPI\Client\tinkoff\api\\PaymentRegistry
```

Получение платежного реестра

Метод вызывается для получения информации по платежному реестру. Вызывать необходимо не чаще, чем раз в 10 минут.<br> В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/payment-registry/manage, где {inn} — ИНН клиента, а {kpp} — КПП клиента.

### Example

```php
<?php
require_once(__DIR__ . '/vendor/autoload.php');


// Configure Bearer authorization: httpAuth
$config = OpenAPI\Client\Configuration::getDefaultConfiguration()->setAccessToken('YOUR_ACCESS_TOKEN');


$apiInstance = new OpenAPI\Client\Api\SalaryApi(
    // If you want use custom http client, pass your client which implements `GuzzleHttp\ClientInterface`.
    // This is optional, `GuzzleHttp\Client` will be used as default.
    new GuzzleHttp\Client(),
    $config
);
$payment_registry_id = 5; // int | Номер платежного реестра

try {
    $result = $apiInstance->salaryGetPaymentRegistry($payment_registry_id);
    print_r($result);
} catch (Exception $e) {
    echo 'Exception when calling SalaryApi->salaryGetPaymentRegistry: ', $e->getMessage(), PHP_EOL;
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **payment_registry_id** | **int**| Номер платежного реестра |

### Return type

[**\OpenAPI\Client\tinkoff\api\\PaymentRegistry**](../Model/PaymentRegistry.md)

### Authorization

[httpAuth](../../README.md#httpAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: `application/json`

[[Back to top]](#) [[Back to API list]](../../README.md#endpoints)
[[Back to Model list]](../../README.md#models)
[[Back to README]](../../README.md)

## `salaryGetPaymentRegistryCreateResult()`

```php
salaryGetPaymentRegistryCreateResult($correlation_id): \OpenAPI\Client\tinkoff\api\\CreatePaymentRegistryResultResponse
```

Получить результат создания черновика платежного реестра

Метод возвращает результат запроса на <a href=\"#operation/SalaryCreatePaymentRegistry\">создание платежного реестра</a>. Результат создания хранится в течение 2-х дней.<br> В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/payment-registry/manage, где {inn} — ИНН клиента, а {kpp} — КПП клиента.

### Example

```php
<?php
require_once(__DIR__ . '/vendor/autoload.php');


// Configure Bearer authorization: httpAuth
$config = OpenAPI\Client\Configuration::getDefaultConfiguration()->setAccessToken('YOUR_ACCESS_TOKEN');


$apiInstance = new OpenAPI\Client\Api\SalaryApi(
    // If you want use custom http client, pass your client which implements `GuzzleHttp\ClientInterface`.
    // This is optional, `GuzzleHttp\Client` will be used as default.
    new GuzzleHttp\Client(),
    $config
);
$correlation_id = cf99df08-0829-4614-8da3-0e440fd23fe0; // string | Идентификатор, связывающий запрос создания с запросом получения ответа

try {
    $result = $apiInstance->salaryGetPaymentRegistryCreateResult($correlation_id);
    print_r($result);
} catch (Exception $e) {
    echo 'Exception when calling SalaryApi->salaryGetPaymentRegistryCreateResult: ', $e->getMessage(), PHP_EOL;
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **correlation_id** | **string**| Идентификатор, связывающий запрос создания с запросом получения ответа |

### Return type

[**\OpenAPI\Client\tinkoff\api\\CreatePaymentRegistryResultResponse**](../Model/CreatePaymentRegistryResultResponse.md)

### Authorization

[httpAuth](../../README.md#httpAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: `application/json`

[[Back to top]](#) [[Back to API list]](../../README.md#endpoints)
[[Back to Model list]](../../README.md#models)
[[Back to README]](../../README.md)

## `salaryPaymentRegistrySubmit()`

```php
salaryPaymentRegistrySubmit($payment_registry_submit_request): \OpenAPI\Client\tinkoff\api\\PaymentRegistrySubmitResponse
```

🔒 Подписание платёжного реестра сотрудников

Запрос является асинхронной операцией — его результат можно получить, вызвав метод <a href=\"#operation/SalaryPaymentRegistrySubmitResult\">Получить результат подписания платежного реестра</a>, передав в него соответствующий <b>correlationId</b>.<br> Данный метод позволяет подписать черновик платёжного реестра. createPaymentRegistryПодписанный реестр исполняется после совершения платежа на сумму реестра на транзитный счёт - <b>47422810900000081042</b>. Создать и оплатить платёжное поручение можно методом <a href=\"#operation/PaymentsCorePay\">Выполнить платёж</a>. В назначении необходимо указать слово \"реестр\" и номер реестра, который будет исполнен платежом. Например: <em>\"Оплата по договору согласно реестру №394 от 24.09.2020. НДС не облагается\"</em> <br> В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/payment-registry/submit, где {inn} — ИНН клиента, а {kpp} — КПП клиента.

### Example

```php
<?php
require_once(__DIR__ . '/vendor/autoload.php');


// Configure Bearer authorization: httpAuth
$config = OpenAPI\Client\Configuration::getDefaultConfiguration()->setAccessToken('YOUR_ACCESS_TOKEN');


$apiInstance = new OpenAPI\Client\Api\SalaryApi(
    // If you want use custom http client, pass your client which implements `GuzzleHttp\ClientInterface`.
    // This is optional, `GuzzleHttp\Client` will be used as default.
    new GuzzleHttp\Client(),
    $config
);
$payment_registry_submit_request = {"correlationId":"cf99df08-0829-4614-8da3-0e440fd23fe0","paymentRegistryId":12}; // \OpenAPI\Client\tinkoff\api\\PaymentRegistrySubmitRequest

try {
    $result = $apiInstance->salaryPaymentRegistrySubmit($payment_registry_submit_request);
    print_r($result);
} catch (Exception $e) {
    echo 'Exception when calling SalaryApi->salaryPaymentRegistrySubmit: ', $e->getMessage(), PHP_EOL;
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **payment_registry_submit_request** | [**\OpenAPI\Client\tinkoff\api\\PaymentRegistrySubmitRequest**](../Model/PaymentRegistrySubmitRequest.md)|  |

### Return type

[**\OpenAPI\Client\tinkoff\api\\PaymentRegistrySubmitResponse**](../Model/PaymentRegistrySubmitResponse.md)

### Authorization

[httpAuth](../../README.md#httpAuth)

### HTTP request headers

- **Content-Type**: `application/json`
- **Accept**: `application/json`

[[Back to top]](#) [[Back to API list]](../../README.md#endpoints)
[[Back to Model list]](../../README.md#models)
[[Back to README]](../../README.md)

## `salaryPaymentRegistrySubmitResult()`

```php
salaryPaymentRegistrySubmitResult($correlation_id): \OpenAPI\Client\tinkoff\api\\PaymentRegistrySubmitResultResponse
```

🔒 Получить результат подписания платёжного реестра сотрудников

Метод возвращает результат запроса на <a href=\"#operation/SalaryPaymentRegistrySubmit\">подписания платежного реестра</a>. Результат создания хранится в течение 2-х дней.<br> В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/payment-registry/submit, где {inn} — ИНН клиента, а {kpp} — КПП клиента.

### Example

```php
<?php
require_once(__DIR__ . '/vendor/autoload.php');


// Configure Bearer authorization: httpAuth
$config = OpenAPI\Client\Configuration::getDefaultConfiguration()->setAccessToken('YOUR_ACCESS_TOKEN');


$apiInstance = new OpenAPI\Client\Api\SalaryApi(
    // If you want use custom http client, pass your client which implements `GuzzleHttp\ClientInterface`.
    // This is optional, `GuzzleHttp\Client` will be used as default.
    new GuzzleHttp\Client(),
    $config
);
$correlation_id = cf99df08-0829-4614-8da3-0e440fd23fe0; // string | Идентификатор, связывающий запрос создания с запросом получения ответа

try {
    $result = $apiInstance->salaryPaymentRegistrySubmitResult($correlation_id);
    print_r($result);
} catch (Exception $e) {
    echo 'Exception when calling SalaryApi->salaryPaymentRegistrySubmitResult: ', $e->getMessage(), PHP_EOL;
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **correlation_id** | **string**| Идентификатор, связывающий запрос создания с запросом получения ответа |

### Return type

[**\OpenAPI\Client\tinkoff\api\\PaymentRegistrySubmitResultResponse**](../Model/PaymentRegistrySubmitResultResponse.md)

### Authorization

[httpAuth](../../README.md#httpAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: `application/json`

[[Back to top]](#) [[Back to API list]](../../README.md#endpoints)
[[Back to Model list]](../../README.md#models)
[[Back to README]](../../README.md)
