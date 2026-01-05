# API Test Plan & Manual Verification

This document outlines the steps to verify the stability of the People & Tax Ecosystem APIs.

## 🔗 Swagger / OpenAPI Documentation
Once the services are running, you can access the interactive API docs at:
- **People Service**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **Tax Engine Service**: [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)

## 🧪 1. People Management Service
**Base URL**: `http://localhost:8080`

### 1.1 Create Person (Full Time Employee)
**Endpoint**: `POST /people`
**Headers**:
- `Content-Type`: `application/json`

**Body**:
```json
{
  "personType": "EMPLOYEE_FULL_TIME",
  "id": 101,
  "name": "Rahul Dravid",
  "email": "rahul.dravid@example.com",
  "annualSalary": 1200000
}
```
**Expected Response**: `201 Created`

### 1.2 Create Person (Contractor)
**Endpoint**: `POST /people`
**Body**:
```json
{
  "personType": "EMPLOYEE_CONTRACTOR",
  "id": 102,
  "name": "Hardik Pandya",
  "email": "hardik@example.com",
  "hourlyRate": 2000,
  "hoursWorked": 160
}
```

### 1.3 Get Person by ID
**Endpoint**: `GET /people/101`
**Expected Response**: JSON object of Rahul Dravid.

### 1.4 Get Monthly Income
**Endpoint**: `GET /people/101/income`
**Expected Response**: `100000.00` (12,00,000 / 12)

---

## 💰 2. Tax Engine Service
**Base URL**: `http://localhost:8081`

### 2.1 Calculate Tax (Standalone)
**Endpoint**: `POST /tax/calculate`
**Body**:
```json
{
  "person": {
      "personType": "EMPLOYEE_FULL_TIME",
      "id": 999,
      "name": "Richie Rich",
      "email": "richie@example.com",
      "annualSalary": 1500000
  },
  "regime": "NEW"
}
```
**Expected Response**: JSON with calculated tax breakdown.

### 2.2 Calculate Tax for Existing Person (Orchestrated)
**Endpoint**: `GET /tax/calculate/101?regime=NEW`
**Description**: Fetches Rahul Dravid (101) from People Service and calculates tax.
**Verification**: Check logs for `X-Correlation-ID` to ensure it matches across both services.

---

## 🛠️ Verification Checklist
- [x] Swagger UI loads for both services.
- [x] `POST /people` creates data successfully.
- [x] `GET /people/{id}` retrieves correct data.
- [x] `POST /tax/calculate` returns valid tax computation.
- [x] `GET /tax/calculate/{id}` works and shows orchestration success.
- [x] Logs show matching `X-Correlation-ID` for the orchestrated call.
