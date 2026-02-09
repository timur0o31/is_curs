# API Endpoints - Примеры запросов

## Тестовые учетные данные

Все пользователи имеют пароль: `password123`

- **Администратор**: admin@hospital.com
- **Врач 1**: doctor1@hospital.com
- **Врач 2**: doctor2@hospital.com
- **Пациент 1**: patient1@example.com
- **Пациент 2**: patient2@example.com
- **Заблокированный**: locked@example.com
- **Отключенный**: disabled@example.com

## Публичные endpoints (без аутентификации)

### 1. Проверка здоровья сервиса
```bash
curl -X GET http://localhost:8080/public/health
```

### 2. Получить информацию о ролях
```bash
curl -X GET http://localhost:8080/public/roles
```

### 3. Получить список разрешений
```bash
curl -X GET http://localhost:8080/public/permissions
```

## Аутентификация

### 1. Вход в систему (Администратор)
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@hospital.com",
    "password": "password123"
  }'
```

**Ответ:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
  "role": "ADMIN",
  "email": "admin@hospital.com",
  "name": "Администратор Иванов"
}
```

### 2. Вход в систему (Врач)
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "doctor1@hospital.com",
    "password": "password123"
  }'
```

### 3. Вход в систему (Пациент)
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "patient1@example.com",
    "password": "password123"
  }'
```

### 4. Регистрация нового пользователя
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "newdoctor@hospital.com",
    "password": "SecurePassword123!",
    "name": "Новый Врач",
    "role": "DOCTOR"
  }'
```

## Endpoints для Администратора

**Примечание:** Замените `{ADMIN_TOKEN}` на токен, полученный при входе администратора.

### 1. Получить список всех пользователей
```bash
curl -X GET http://localhost:8080/admin/users \
  -H "Authorization: Bearer {ADMIN_TOKEN}"
```

### 2. Получить пользователя по ID
```bash
curl -X GET http://localhost:8080/admin/users/1 \
  -H "Authorization: Bearer {ADMIN_TOKEN}"
```

### 3. Обновить пользователя
```bash
curl -X PUT http://localhost:8080/admin/users/2 \
  -H "Authorization: Bearer {ADMIN_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Обновленное Имя",
    "email": "newemail@hospital.com"
  }'
```

### 4. Удалить пользователя
```bash
curl -X DELETE http://localhost:8080/admin/users/7 \
  -H "Authorization: Bearer {ADMIN_TOKEN}"
```

### 5. Заблокировать пользователя
```bash
curl -X POST http://localhost:8080/admin/users/5/lock \
  -H "Authorization: Bearer {ADMIN_TOKEN}"
```

### 6. Разблокировать пользователя
```bash
curl -X POST http://localhost:8080/admin/users/6/unlock \
  -H "Authorization: Bearer {ADMIN_TOKEN}"
```

### 7. Отключить учетную запись
```bash
curl -X POST http://localhost:8080/admin/users/5/disable \
  -H "Authorization: Bearer {ADMIN_TOKEN}"
```

### 8. Включить учетную запись
```bash
curl -X POST http://localhost:8080/admin/users/7/enable \
  -H "Authorization: Bearer {ADMIN_TOKEN}"
```

## Endpoints для Врача

**Примечание:** Замените `{DOCTOR_TOKEN}` на токен врача.

### 1. Панель врача
```bash
curl -X GET http://localhost:8080/doctor/dashboard \
  -H "Authorization: Bearer {DOCTOR_TOKEN}"
```

### 2. Список пациентов
```bash
curl -X GET http://localhost:8080/doctor/patients \
  -H "Authorization: Bearer {DOCTOR_TOKEN}"
```

### 3. Получить медицинскую карту пациента
```bash
curl -X GET http://localhost:8080/doctor/medical-cards/1 \
  -H "Authorization: Bearer {DOCTOR_TOKEN}"
```

### 4. Обновить медицинскую карту
```bash
curl -X POST http://localhost:8080/doctor/medical-cards/1 \
  -H "Authorization: Bearer {DOCTOR_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "diagnosis": "Новый диагноз",
    "notes": "Примечания врача"
  }'
```

### 5. Список сеансов
```bash
curl -X GET http://localhost:8080/doctor/sessions \
  -H "Authorization: Bearer {DOCTOR_TOKEN}"
```

### 6. Создать сеанс
```bash
curl -X POST http://localhost:8080/doctor/sessions \
  -H "Authorization: Bearer {DOCTOR_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": 1,
    "procedureId": 2,
    "date": "2026-02-15",
    "time": "14:00"
  }'
```

### 7. Создать рецепт
```bash
curl -X POST http://localhost:8080/doctor/prescriptions \
  -H "Authorization: Bearer {DOCTOR_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": 1,
    "medicamentId": 3,
    "dosage": 2,
    "frequency": 3,
    "notes": "Принимать после еды"
  }'
```

### 8. Получить рецепт
```bash
curl -X GET http://localhost:8080/doctor/prescriptions/1 \
  -H "Authorization: Bearer {DOCTOR_TOKEN}"
```

## Endpoints для Пациента

**Примечание:** Замените `{PATIENT_TOKEN}` на токен пациента.

### 1. Панель пациента
```bash
curl -X GET http://localhost:8080/patient/dashboard \
  -H "Authorization: Bearer {PATIENT_TOKEN}"
```

### 2. Моя медицинская карта
```bash
curl -X GET http://localhost:8080/patient/medical-card \
  -H "Authorization: Bearer {PATIENT_TOKEN}"
```

### 3. Мои заявки на пребывание
```bash
curl -X GET http://localhost:8080/patient/stay-requests \
  -H "Authorization: Bearer {PATIENT_TOKEN}"
```

### 4. Создать заявку на пребывание
```bash
curl -X POST http://localhost:8080/patient/stay-requests \
  -H "Authorization: Bearer {PATIENT_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "type": "check-in",
    "admissionDate": "2026-03-01",
    "dischargeDate": "2026-03-15"
  }'
```

### 5. Мои сеансы
```bash
curl -X GET http://localhost:8080/patient/sessions \
  -H "Authorization: Bearer {PATIENT_TOKEN}"
```

### 6. Мои рецепты
```bash
curl -X GET http://localhost:8080/patient/prescriptions \
  -H "Authorization: Bearer {PATIENT_TOKEN}"
```

### 7. Информация о комнате
```bash
curl -X GET http://localhost:8080/patient/room \
  -H "Authorization: Bearer {PATIENT_TOKEN}"
```

### 8. Информация о шкафчике
```bash
curl -X GET http://localhost:8080/patient/locker \
  -H "Authorization: Bearer {PATIENT_TOKEN}"
```

## Тестирование прав доступа

### Попытка доступа пациента к endpoint врача (должна быть отклонена)
```bash
curl -X POST http://localhost:8080/doctor/sessions \
  -H "Authorization: Bearer {PATIENT_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{}'
```

**Ожидаемый ответ:** 403 Forbidden

### Попытка доступа врача к endpoint администратора (должна быть отклонена)
```bash
curl -X DELETE http://localhost:8080/admin/users/1 \
  -H "Authorization: Bearer {DOCTOR_TOKEN}"
```

**Ожидаемый ответ:** 403 Forbidden

### Попытка входа заблокированного пользователя
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "locked@example.com",
    "password": "password123"
  }'
```

**Ожидаемый ответ:** 409 Conflict - "Учетная запись заблокирована"

## PowerShell примеры

### Вход и сохранение токена
```powershell
$response = Invoke-RestMethod -Uri "http://localhost:8080/auth/login" `
  -Method POST `
  -ContentType "application/json" `
  -Body '{"email":"admin@hospital.com","password":"password123"}'

$token = $response.token
Write-Host "Token: $token"
```

### Использование токена в запросе
```powershell
$headers = @{
  "Authorization" = "Bearer $token"
}

Invoke-RestMethod -Uri "http://localhost:8080/admin/users" `
  -Method GET `
  -Headers $headers
```

## Примечания

1. Замените `http://localhost:8080` на ваш URL сервера
2. Токены действительны 24 часа (настраивается в application.properties)
3. Все пароли должны быть надежными в production среде
4. При ошибке 401 (Unauthorized) - токен истек или недействителен
5. При ошибке 403 (Forbidden) - недостаточно прав для выполнения операции

