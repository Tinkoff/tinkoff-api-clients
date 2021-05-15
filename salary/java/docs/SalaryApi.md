# SalaryApi

All URIs are relative to *https://secured-openapi.business.tinkoff.ru*

Method | HTTP request | Description
------------- | ------------- | -------------
[**salaryCreateEmployee**](SalaryApi.md#salaryCreateEmployee) | **POST** /api/v1/salary/employees/create | Создать черновики анкет сотрудников
[**salaryCreatePaymentRegistry**](SalaryApi.md#salaryCreatePaymentRegistry) | **POST** /api/v1/salary/payment-registry/create | Создание черновика платежного реестра
[**salaryGetEmployeesCreateResult**](SalaryApi.md#salaryGetEmployeesCreateResult) | **GET** /api/v1/salary/employees/create/result | Получить результат создания черновиков анкет сотрудников
[**salaryGetEmployeesList**](SalaryApi.md#salaryGetEmployeesList) | **POST** /api/v1/salary/employees/list | Получить информацию по сотрудникам
[**salaryGetPaymentRegistry**](SalaryApi.md#salaryGetPaymentRegistry) | **GET** /api/v1/salary/payment-registry/{paymentRegistryId} | Получение платежного реестра
[**salaryGetPaymentRegistryCreateResult**](SalaryApi.md#salaryGetPaymentRegistryCreateResult) | **GET** /api/v1/salary/payment-registry/create/result | Получить результат создания черновика платежного реестра
[**salaryPaymentRegistrySubmit**](SalaryApi.md#salaryPaymentRegistrySubmit) | **POST** /api/v1/salary/payment-registry/submit | 🔒 Подписание платёжного реестра сотрудников
[**salaryPaymentRegistrySubmitResult**](SalaryApi.md#salaryPaymentRegistrySubmitResult) | **GET** /api/v1/salary/payment-registry/submit/result | 🔒 Получить результат подписания платёжного реестра сотрудников


<a name="salaryCreateEmployee"></a>
# **salaryCreateEmployee**
> CreateEmployeesResponse salaryCreateEmployee(createEmployeesRequest)

Создать черновики анкет сотрудников

 Запрос является асинхронной операцией — его результат можно получить, вызвав метод &lt;a href&#x3D;\&quot;#operation/SalaryGetEmployeesCreateResult\&quot;&gt;Получить результат создания сотрудников&lt;/a&gt;, передав в него соответствующий &lt;b&gt;correlationId&lt;/b&gt;. В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/employees/manage, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         

### Example
```java
// Import classes:
import ru.tinkoff.api.ApiClient;
import ru.tinkoff.api.ApiException;
import ru.tinkoff.api.Configuration;
import ru.tinkoff.api.auth.*;
import ru.tinkoff.api.models.*;
import ru.tinkoff.api.salary.SalaryApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://secured-openapi.business.tinkoff.ru");
    
    // Configure HTTP bearer authorization: httpAuth
    HttpBearerAuth httpAuth = (HttpBearerAuth) defaultClient.getAuthentication("httpAuth");
    httpAuth.setBearerToken("BEARER TOKEN");

    SalaryApi apiInstance = new SalaryApi(defaultClient);
    CreateEmployeesRequest createEmployeesRequest = new CreateEmployeesRequest(); // CreateEmployeesRequest | 
    try {
      CreateEmployeesResponse result = apiInstance.salaryCreateEmployee(createEmployeesRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SalaryApi#salaryCreateEmployee");
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
 **createEmployeesRequest** | [**CreateEmployeesRequest**](CreateEmployeesRequest.md)|  |

### Return type

[**CreateEmployeesResponse**](CreateEmployeesResponse.md)

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

<a name="salaryCreatePaymentRegistry"></a>
# **salaryCreatePaymentRegistry**
> CreatePaymentRegistryResponse salaryCreatePaymentRegistry(createPaymentRegistryRequest)

Создание черновика платежного реестра

 Запрос является асинхронной операцией — его результат можно получить, вызвав метод &lt;a href&#x3D;\&quot;#operation/SalaryGetPaymentRegistryCreateResult\&quot;&gt;Получить результат создания платежного реестра&lt;/a&gt;, передав в него соответствующий &lt;b&gt;correlationId&lt;/b&gt;.&lt;br&gt; &lt;u&gt;Вы можете добавлять в реестр сотрудников, которые находятся в статусах ACTIVE и FIRED&lt;/u&gt;&lt;br&gt; В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/payment-registry/manage, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         

### Example
```java
// Import classes:
import ru.tinkoff.api.ApiClient;
import ru.tinkoff.api.ApiException;
import ru.tinkoff.api.Configuration;
import ru.tinkoff.api.auth.*;
import ru.tinkoff.api.models.*;
import ru.tinkoff.api.salary.SalaryApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://secured-openapi.business.tinkoff.ru");
    
    // Configure HTTP bearer authorization: httpAuth
    HttpBearerAuth httpAuth = (HttpBearerAuth) defaultClient.getAuthentication("httpAuth");
    httpAuth.setBearerToken("BEARER TOKEN");

    SalaryApi apiInstance = new SalaryApi(defaultClient);
    CreatePaymentRegistryRequest createPaymentRegistryRequest = new CreatePaymentRegistryRequest(); // CreatePaymentRegistryRequest | 
    try {
      CreatePaymentRegistryResponse result = apiInstance.salaryCreatePaymentRegistry(createPaymentRegistryRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SalaryApi#salaryCreatePaymentRegistry");
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
 **createPaymentRegistryRequest** | [**CreatePaymentRegistryRequest**](CreatePaymentRegistryRequest.md)|  |

### Return type

[**CreatePaymentRegistryResponse**](CreatePaymentRegistryResponse.md)

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

<a name="salaryGetEmployeesCreateResult"></a>
# **salaryGetEmployeesCreateResult**
> CreateEmployeeResultResponse salaryGetEmployeesCreateResult(correlationId)

Получить результат создания черновиков анкет сотрудников

 Метод возвращает результат запроса на &lt;a href&#x3D;\&quot;#operation/SalaryCreateEmployee\&quot;&gt;создание черновиков анкет сотрудников&lt;/a&gt;. Ответ на запрос создания хранится в течение 2-х дней.&lt;br&gt; В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/employees/manage, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         

### Example
```java
// Import classes:
import ru.tinkoff.api.ApiClient;
import ru.tinkoff.api.ApiException;
import ru.tinkoff.api.Configuration;
import ru.tinkoff.api.auth.*;
import ru.tinkoff.api.models.*;
import ru.tinkoff.api.salary.SalaryApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://secured-openapi.business.tinkoff.ru");
    
    // Configure HTTP bearer authorization: httpAuth
    HttpBearerAuth httpAuth = (HttpBearerAuth) defaultClient.getAuthentication("httpAuth");
    httpAuth.setBearerToken("BEARER TOKEN");

    SalaryApi apiInstance = new SalaryApi(defaultClient);
    String correlationId = "cf99df08-0829-4614-8da3-0e440fd23fe0"; // String | Уникальный идентификатор(тип <a href=\"https://en.wikipedia.org/wiki/Universally_unique_identifier\">UUID</a>), связывающий запрос создания с запросом получения ответа (должен быть сформирован на стороне клиента)
    try {
      CreateEmployeeResultResponse result = apiInstance.salaryGetEmployeesCreateResult(correlationId);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SalaryApi#salaryGetEmployeesCreateResult");
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
 **correlationId** | **String**| Уникальный идентификатор(тип &lt;a href&#x3D;\&quot;https://en.wikipedia.org/wiki/Universally_unique_identifier\&quot;&gt;UUID&lt;/a&gt;), связывающий запрос создания с запросом получения ответа (должен быть сформирован на стороне клиента) |

### Return type

[**CreateEmployeeResultResponse**](CreateEmployeeResultResponse.md)

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

<a name="salaryGetEmployeesList"></a>
# **salaryGetEmployeesList**
> EmployeeResponse salaryGetEmployeesList(employeesInfoRequest)

Получить информацию по сотрудникам

 Метод вызывается для получения информации по сотрудникам. Вызывать необходимо не чаще, чем раз в 10 минут.&lt;br&gt; Заявка для добавления сотрудника создается в статусе DRAFT, после чего ее необходимо отправить в Личном кабинете. Отправленный сотрудник перейдет в статус ACTIVE.&lt;br&gt; В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/employees/manage, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         

### Example
```java
// Import classes:
import ru.tinkoff.api.ApiClient;
import ru.tinkoff.api.ApiException;
import ru.tinkoff.api.Configuration;
import ru.tinkoff.api.auth.*;
import ru.tinkoff.api.models.*;
import ru.tinkoff.api.salary.SalaryApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://secured-openapi.business.tinkoff.ru");
    
    // Configure HTTP bearer authorization: httpAuth
    HttpBearerAuth httpAuth = (HttpBearerAuth) defaultClient.getAuthentication("httpAuth");
    httpAuth.setBearerToken("BEARER TOKEN");

    SalaryApi apiInstance = new SalaryApi(defaultClient);
    EmployeesInfoRequest employeesInfoRequest = new EmployeesInfoRequest(); // EmployeesInfoRequest | Список идентификаторов сотрудников
    try {
      EmployeeResponse result = apiInstance.salaryGetEmployeesList(employeesInfoRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SalaryApi#salaryGetEmployeesList");
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
 **employeesInfoRequest** | [**EmployeesInfoRequest**](EmployeesInfoRequest.md)| Список идентификаторов сотрудников |

### Return type

[**EmployeeResponse**](EmployeeResponse.md)

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

<a name="salaryGetPaymentRegistry"></a>
# **salaryGetPaymentRegistry**
> PaymentRegistry salaryGetPaymentRegistry(paymentRegistryId)

Получение платежного реестра

 Метод вызывается для получения информации по платежному реестру. Вызывать необходимо не чаще, чем раз в 10 минут.&lt;br&gt; В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/payment-registry/manage, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         

### Example
```java
// Import classes:
import ru.tinkoff.api.ApiClient;
import ru.tinkoff.api.ApiException;
import ru.tinkoff.api.Configuration;
import ru.tinkoff.api.auth.*;
import ru.tinkoff.api.models.*;
import ru.tinkoff.api.salary.SalaryApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://secured-openapi.business.tinkoff.ru");
    
    // Configure HTTP bearer authorization: httpAuth
    HttpBearerAuth httpAuth = (HttpBearerAuth) defaultClient.getAuthentication("httpAuth");
    httpAuth.setBearerToken("BEARER TOKEN");

    SalaryApi apiInstance = new SalaryApi(defaultClient);
    Integer paymentRegistryId = 5; // Integer | Номер платежного реестра
    try {
      PaymentRegistry result = apiInstance.salaryGetPaymentRegistry(paymentRegistryId);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SalaryApi#salaryGetPaymentRegistry");
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
 **paymentRegistryId** | **Integer**| Номер платежного реестра |

### Return type

[**PaymentRegistry**](PaymentRegistry.md)

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

<a name="salaryGetPaymentRegistryCreateResult"></a>
# **salaryGetPaymentRegistryCreateResult**
> CreatePaymentRegistryResultResponse salaryGetPaymentRegistryCreateResult(correlationId)

Получить результат создания черновика платежного реестра

 Метод возвращает результат запроса на &lt;a href&#x3D;\&quot;#operation/SalaryCreatePaymentRegistry\&quot;&gt;создание платежного реестра&lt;/a&gt;. Результат создания хранится в течение 2-х дней.&lt;br&gt; В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/payment-registry/manage, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         

### Example
```java
// Import classes:
import ru.tinkoff.api.ApiClient;
import ru.tinkoff.api.ApiException;
import ru.tinkoff.api.Configuration;
import ru.tinkoff.api.auth.*;
import ru.tinkoff.api.models.*;
import ru.tinkoff.api.salary.SalaryApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://secured-openapi.business.tinkoff.ru");
    
    // Configure HTTP bearer authorization: httpAuth
    HttpBearerAuth httpAuth = (HttpBearerAuth) defaultClient.getAuthentication("httpAuth");
    httpAuth.setBearerToken("BEARER TOKEN");

    SalaryApi apiInstance = new SalaryApi(defaultClient);
    String correlationId = "cf99df08-0829-4614-8da3-0e440fd23fe0"; // String | Идентификатор, связывающий запрос создания с запросом получения ответа
    try {
      CreatePaymentRegistryResultResponse result = apiInstance.salaryGetPaymentRegistryCreateResult(correlationId);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SalaryApi#salaryGetPaymentRegistryCreateResult");
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
 **correlationId** | **String**| Идентификатор, связывающий запрос создания с запросом получения ответа |

### Return type

[**CreatePaymentRegistryResultResponse**](CreatePaymentRegistryResultResponse.md)

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

<a name="salaryPaymentRegistrySubmit"></a>
# **salaryPaymentRegistrySubmit**
> PaymentRegistrySubmitResponse salaryPaymentRegistrySubmit(paymentRegistrySubmitRequest)

🔒 Подписание платёжного реестра сотрудников

 Запрос является асинхронной операцией — его результат можно получить, вызвав метод &lt;a href&#x3D;\&quot;#operation/SalaryPaymentRegistrySubmitResult\&quot;&gt;Получить результат подписания платежного реестра&lt;/a&gt;, передав в него соответствующий &lt;b&gt;correlationId&lt;/b&gt;.&lt;br&gt; Данный метод позволяет подписать черновик платёжного реестра. createPaymentRegistryПодписанный реестр исполняется после совершения платежа на сумму реестра на транзитный счёт - &lt;b&gt;47422810900000081042&lt;/b&gt;. Создать и оплатить платёжное поручение можно методом &lt;a href&#x3D;\&quot;#operation/PaymentsCorePay\&quot;&gt;Выполнить платёж&lt;/a&gt;. В назначении необходимо указать слово \&quot;реестр\&quot; и номер реестра, который будет исполнен платежом. Например: &lt;em&gt;\&quot;Оплата по договору согласно реестру №394 от 24.09.2020. НДС не облагается\&quot;&lt;/em&gt; &lt;br&gt; В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/payment-registry/submit, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         

### Example
```java
// Import classes:
import ru.tinkoff.api.ApiClient;
import ru.tinkoff.api.ApiException;
import ru.tinkoff.api.Configuration;
import ru.tinkoff.api.auth.*;
import ru.tinkoff.api.models.*;
import ru.tinkoff.api.salary.SalaryApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://secured-openapi.business.tinkoff.ru");
    
    // Configure HTTP bearer authorization: httpAuth
    HttpBearerAuth httpAuth = (HttpBearerAuth) defaultClient.getAuthentication("httpAuth");
    httpAuth.setBearerToken("BEARER TOKEN");

    SalaryApi apiInstance = new SalaryApi(defaultClient);
    PaymentRegistrySubmitRequest paymentRegistrySubmitRequest = new PaymentRegistrySubmitRequest(); // PaymentRegistrySubmitRequest | 
    try {
      PaymentRegistrySubmitResponse result = apiInstance.salaryPaymentRegistrySubmit(paymentRegistrySubmitRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SalaryApi#salaryPaymentRegistrySubmit");
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
 **paymentRegistrySubmitRequest** | [**PaymentRegistrySubmitRequest**](PaymentRegistrySubmitRequest.md)|  |

### Return type

[**PaymentRegistrySubmitResponse**](PaymentRegistrySubmitResponse.md)

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

<a name="salaryPaymentRegistrySubmitResult"></a>
# **salaryPaymentRegistrySubmitResult**
> PaymentRegistrySubmitResultResponse salaryPaymentRegistrySubmitResult(correlationId)

🔒 Получить результат подписания платёжного реестра сотрудников

 Метод возвращает результат запроса на &lt;a href&#x3D;\&quot;#operation/SalaryPaymentRegistrySubmit\&quot;&gt;подписания платежного реестра&lt;/a&gt;. Результат создания хранится в течение 2-х дней.&lt;br&gt; В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/payment-registry/submit, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         

### Example
```java
// Import classes:
import ru.tinkoff.api.ApiClient;
import ru.tinkoff.api.ApiException;
import ru.tinkoff.api.Configuration;
import ru.tinkoff.api.auth.*;
import ru.tinkoff.api.models.*;
import ru.tinkoff.api.salary.SalaryApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://secured-openapi.business.tinkoff.ru");
    
    // Configure HTTP bearer authorization: httpAuth
    HttpBearerAuth httpAuth = (HttpBearerAuth) defaultClient.getAuthentication("httpAuth");
    httpAuth.setBearerToken("BEARER TOKEN");

    SalaryApi apiInstance = new SalaryApi(defaultClient);
    String correlationId = "cf99df08-0829-4614-8da3-0e440fd23fe0"; // String | Идентификатор, связывающий запрос создания с запросом получения ответа
    try {
      PaymentRegistrySubmitResultResponse result = apiInstance.salaryPaymentRegistrySubmitResult(correlationId);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SalaryApi#salaryPaymentRegistrySubmitResult");
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
 **correlationId** | **String**| Идентификатор, связывающий запрос создания с запросом получения ответа |

### Return type

[**PaymentRegistrySubmitResultResponse**](PaymentRegistrySubmitResultResponse.md)

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

