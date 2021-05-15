# PaymentsApi

All URIs are relative to *https://secured-openapi.business.tinkoff.ru*

Method | HTTP request | Description
------------- | ------------- | -------------
[**paymentsCoreGetStatus**](PaymentsApi.md#paymentsCoreGetStatus) | **GET** /api/v1/payment/{paymentId} | 🔒 Получить статус платежа
[**paymentsCorePay**](PaymentsApi.md#paymentsCorePay) | **POST** /api/v1/payment/ruble-transfer/pay | 🔒 Выполнить платёж
[**postApiV1PaymentCreate**](PaymentsApi.md#postApiV1PaymentCreate) | **POST** /api/v1/payment/create | Создать черновик платежного поручения
[**sbpPay**](PaymentsApi.md#sbpPay) | **POST** /api/v1/payment/sbp/pay | 🔒 Выполнить платёж через систему быстрых платежей (СБП)


<a name="paymentsCoreGetStatus"></a>
# **paymentsCoreGetStatus**
> PaymentStatusResponse paymentsCoreGetStatus(paymentId)

🔒 Получить статус платежа

 Данный метод позволяет узнать статус выплаты, произведенной с помощью методов  &lt;a href&#x3D;\&quot;#payment/PaymentsCorePay\&quot;&gt;Выполнить платеж&lt;/a&gt; и &lt;a href&#x3D;\&quot;#payment/SbpPay\&quot;&gt;Выполнить платёж через систему быстрых платежей (СБП)&lt;/a&gt;. В поле scopes у токена должен присутствовать хотя бы один scope вида: opensme/inn/[{inn}]/kpp/[{kpp}]/payments/rub-pay, где {inn} — ИНН клиента, а {kpp} — КПП клиента. opensme/inn/[{inn}]/kpp/[{kpp}]/payments/sbp-pay, где {inn} — ИНН клиента, а {kpp} — КПП клиента.  &lt;b&gt;Примечание для партнёров&lt;/b&gt;: В этом методе учитывается clientId, если ваш clientId был изменён, вы не сможете получить статус платежа, созданного при использовании старого clientId. clientId выдается при регистрации партнера в tinkoff и отправляется на почту. 

### Example
```java
// Import classes:
import ru.tinkoff.api.ApiClient;
import ru.tinkoff.api.ApiException;
import ru.tinkoff.api.Configuration;
import ru.tinkoff.api.auth.*;
import ru.tinkoff.api.models.*;
import ru.tinkoff.api.payments.PaymentsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://secured-openapi.business.tinkoff.ru");
    
    // Configure HTTP bearer authorization: httpAuth
    HttpBearerAuth httpAuth = (HttpBearerAuth) defaultClient.getAuthentication("httpAuth");
    httpAuth.setBearerToken("BEARER TOKEN");

    PaymentsApi apiInstance = new PaymentsApi(defaultClient);
    String paymentId = "paymentId_example"; // String | Идентификатор платежа
    try {
      PaymentStatusResponse result = apiInstance.paymentsCoreGetStatus(paymentId);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling PaymentsApi#paymentsCoreGetStatus");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **paymentId** | **String**| Идентификатор платежа |

### Return type

[**PaymentStatusResponse**](PaymentStatusResponse.md)

### Authorization

[httpAuth](../README.md#httpAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** |  |  -  |
**400** | Некорректный запрос |  -  |
**401** | Ошибка аутентификации |  -  |
**403** | Ошибка авторизации |  -  |
**422** | Ошибка при обработке данных |  -  |
**429** | Слишком много запросов |  -  |
**500** | Ошибка сервера |  -  |

<a name="paymentsCorePay"></a>
# **paymentsCorePay**
> paymentsCorePay(createPaymentRequest)

🔒 Выполнить платёж

 Данный метод позволяет выплачивать деньги с рублевых счетов компании. Выплаты могут производиться на счета юрлиц и физлиц в банках РФ. При передаче информации в блоке \&quot;налоги\&quot; появляется возможность совершать налоговые платежи и налоговые платежи за третьих лиц. Выплата денег со счета производится асинхронно. Результат запроса на выплату можно получить, вызвав метод &lt;a href&#x3D;\&quot;#operation/PaymentsCoreGetStatus\&quot;&gt;Получить статуса платежа&lt;/a&gt;, передав в него соответствующий &lt;b&gt;paymentId&lt;/b&gt;. В поле scope у токена должен присутствовать доступ вида  opensme/inn/[{inn}]/kpp/[{kpp}]/payments/rub-pay, где {inn} — ИНН клиента, а {kpp} — КПП клиента.  &lt;b&gt;Все вызовы метода дедуплицируются:&lt;/b&gt;  &lt;b&gt;Селф-сервис:&lt;/b&gt; Дедупликация происходит по paymentId в контексте вашей компании. Это значит, что paymentId должен быть уникален в рамках всех платежей от лица вашей компании. Если будет произведено два и более вызова метода с одинаковыми paymentId в рамках одной компании, то будет создан только один платеж.  &lt;b&gt;Партерский сценарий:&lt;/b&gt; Дедупликация происходит по paymentId + clientId. Это значит, что связка paymentId + clientId должна быть уникальна. Если будет произведено два и более вызова метода с одинаковыми paymentId и clientId, то будет создан только один платеж. clientId выдается при регистрации партнера в tinkoff и отправляется на почту.  &lt;b&gt;Лимиты:&lt;/b&gt; По умолчанию на выполнение платежей через API установлены следующие лимиты: &lt;ul&gt;   &lt;li&gt;максимальная сумма одного платежа - 100000 рублей&lt;/li&gt;   &lt;li&gt;максимальная сумма платежей в день - 100000 рублей&lt;/li&gt;   &lt;li&gt;максимальная сумма платежей в месяц - 1000000 рублей&lt;/li&gt;   &lt;li&gt;максимальное количество платежей в день на одного контрагента - 3&lt;/li&gt; &lt;/ul&gt; Для изменения лимитов по вашей компании напишите на openapi@tinkoff.ru. 

### Example
```java
// Import classes:
import ru.tinkoff.api.ApiClient;
import ru.tinkoff.api.ApiException;
import ru.tinkoff.api.Configuration;
import ru.tinkoff.api.auth.*;
import ru.tinkoff.api.models.*;
import ru.tinkoff.api.payments.PaymentsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://secured-openapi.business.tinkoff.ru");
    
    // Configure HTTP bearer authorization: httpAuth
    HttpBearerAuth httpAuth = (HttpBearerAuth) defaultClient.getAuthentication("httpAuth");
    httpAuth.setBearerToken("BEARER TOKEN");

    PaymentsApi apiInstance = new PaymentsApi(defaultClient);
    CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest(); // CreatePaymentRequest | 
    try {
      apiInstance.paymentsCorePay(createPaymentRequest);
    } catch (ApiException e) {
      System.err.println("Exception when calling PaymentsApi#paymentsCorePay");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **createPaymentRequest** | [**CreatePaymentRequest**](CreatePaymentRequest.md)|  |

### Return type

null (empty response body)

### Authorization

[httpAuth](../README.md#httpAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**201** | Платеж поставлен в очередь на исполнение |  -  |
**400** | Некорректный запрос |  -  |
**401** | Ошибка аутентификации |  -  |
**403** | Ошибка авторизации |  -  |
**422** | Ошибка при обработке данных |  -  |
**429** | Слишком много запросов |  -  |
**500** | Ошибка сервера |  -  |

<a name="postApiV1PaymentCreate"></a>
# **postApiV1PaymentCreate**
> CreatePaymentDraftResponse postApiV1PaymentCreate(createPaymentDraftRequest)

Создать черновик платежного поручения

Необходимо согласие пользователя на: \&quot;Доступ к созданию черновиков платежных поручений\&quot;. Подробнее про поля платежного поручения и их форматы можно почитать здесь: https://glavkniga.ru/situations/k503207. В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/payments/draft/create, где {inn} — ИНН клиента, а {kpp} — КПП клиента.

### Example
```java
// Import classes:
import ru.tinkoff.api.ApiClient;
import ru.tinkoff.api.ApiException;
import ru.tinkoff.api.Configuration;
import ru.tinkoff.api.auth.*;
import ru.tinkoff.api.models.*;
import ru.tinkoff.api.payments.PaymentsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://secured-openapi.business.tinkoff.ru");
    
    // Configure HTTP bearer authorization: httpAuth
    HttpBearerAuth httpAuth = (HttpBearerAuth) defaultClient.getAuthentication("httpAuth");
    httpAuth.setBearerToken("BEARER TOKEN");

    PaymentsApi apiInstance = new PaymentsApi(defaultClient);
    CreatePaymentDraftRequest createPaymentDraftRequest = new CreatePaymentDraftRequest(); // CreatePaymentDraftRequest | 
    try {
      CreatePaymentDraftResponse result = apiInstance.postApiV1PaymentCreate(createPaymentDraftRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling PaymentsApi#postApiV1PaymentCreate");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **createPaymentDraftRequest** | [**CreatePaymentDraftRequest**](CreatePaymentDraftRequest.md)|  |

### Return type

[**CreatePaymentDraftResponse**](CreatePaymentDraftResponse.md)

### Authorization

[httpAuth](../README.md#httpAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** |  |  -  |
**400** | Некорректный запрос |  -  |
**401** | Ошибка аутентификации |  -  |
**403** | Ошибка авторизации |  -  |
**422** | Ошибка при обработке данных |  -  |
**429** | Слишком много запросов |  -  |
**500** | Ошибка сервера |  -  |

<a name="sbpPay"></a>
# **sbpPay**
> sbpPay(createSbpPaymentRequest)

🔒 Выполнить платёж через систему быстрых платежей (СБП)

 Данный метод позволяет производить выплаты с рублевых счетов компании через систему быстрых платежей (СБП). Выплаты производятся по номеру телефона на счета физлиц в банках РФ, подключенных к системе быстрых платежей. Выплата денег со счета производится асинхронно. Результат запроса на выплату можно получить, вызвав метод &lt;a href&#x3D;\&quot;#operation/PaymentsCoreGetStatus\&quot;&gt;Получить статуса платежа&lt;/a&gt;, передав в него соответствующий &lt;b&gt;paymentId&lt;/b&gt;. В поле scope у токена должен присутствовать доступ вида  opensme/inn/[{inn}]/kpp/[{kpp}]/payments/sbp-pay, где {inn} — ИНН клиента, а {kpp} — КПП клиента.  Для совершения выплаты необходимо указать идентификатор банка {bankId} в системе СБП.  &lt;b&gt;Список банков, поддерживающих переводы физлицам через систему СБП:&lt;/b&gt;  Наименование | bankId ----------------------|------------- Тинькофф Банк | 100000000004 ВТБ | 100000000005 ЮМани | 100000000022 Банк АВАНГАРД | 100000000028 Газэнергобанк | 100000000043 Экспобанк | 100000000044 КБ ПЛАТИНА | 100000000048 Банк ВБРР | 100000000049 ТОЧКА (ФК ОТКРЫТИЕ) | 100000000065 АБ РОССИЯ | 100000000095 КБ Модульбанк | 100000000099 Русское финансовое общество | 100000000104 МСП Банк | 100000000246  &lt;b&gt;Все вызовы метода дедуплицируются:&lt;/b&gt;  &lt;b&gt;Селф-сервис:&lt;/b&gt; Дедупликация происходит по paymentId в контексте вашей компании. Это значит, что paymentId должен быть уникален в рамках всех платежей от лица вашей компании. Если будет произведено два и более вызова метода с одинаковыми paymentId в рамках одной компании, то будет создан только один платеж.  &lt;b&gt;Партерский сценарий:&lt;/b&gt; Дедупликация происходит по paymentId + clientId. Это значит, что связка paymentId + clientId должна быть уникальна. Если будет произведено два и более вызова метода с одинаковыми paymentId и clientId, то будет создан только один платеж. clientId выдается при регистрации партнера в tinkoff и отправляется на почту.  &lt;b&gt;Лимиты:&lt;/b&gt; По умолчанию на выполнение платежей через API установлены следующие лимиты: &lt;ul&gt;   &lt;li&gt;максимальная сумма одного платежа - 100000 рублей&lt;/li&gt;   &lt;li&gt;максимальная сумма платежей в день - 100000 рублей&lt;/li&gt;   &lt;li&gt;максимальная сумма платежей в месяц - 1000000 рублей&lt;/li&gt;   &lt;li&gt;максимальное количество платежей в день на одного контрагента - 3&lt;/li&gt; &lt;/ul&gt; Для изменения лимитов по вашей компании напишите на openapi@tinkoff.ru. 

### Example
```java
// Import classes:
import ru.tinkoff.api.ApiClient;
import ru.tinkoff.api.ApiException;
import ru.tinkoff.api.Configuration;
import ru.tinkoff.api.auth.*;
import ru.tinkoff.api.models.*;
import ru.tinkoff.api.payments.PaymentsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://secured-openapi.business.tinkoff.ru");
    
    // Configure HTTP bearer authorization: httpAuth
    HttpBearerAuth httpAuth = (HttpBearerAuth) defaultClient.getAuthentication("httpAuth");
    httpAuth.setBearerToken("BEARER TOKEN");

    PaymentsApi apiInstance = new PaymentsApi(defaultClient);
    CreateSbpPaymentRequest createSbpPaymentRequest = new CreateSbpPaymentRequest(); // CreateSbpPaymentRequest | 
    try {
      apiInstance.sbpPay(createSbpPaymentRequest);
    } catch (ApiException e) {
      System.err.println("Exception when calling PaymentsApi#sbpPay");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **createSbpPaymentRequest** | [**CreateSbpPaymentRequest**](CreateSbpPaymentRequest.md)|  |

### Return type

null (empty response body)

### Authorization

[httpAuth](../README.md#httpAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**201** | Платеж поставлен в очередь на исполнение |  -  |
**400** | Некорректный запрос |  -  |
**401** | Ошибка аутентификации |  -  |
**403** | Ошибка авторизации |  -  |
**422** | Ошибка при обработке данных |  -  |
**429** | Слишком много запросов |  -  |
**500** | Ошибка сервера |  -  |

