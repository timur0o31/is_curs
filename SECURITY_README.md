# Ролевая модель доступа

## Обзор

Система использует многоуровневую ролевую модель доступа (RBAC - Role-Based Access Control) с детализированными разрешениями.

## Роли

### ADMIN (Администратор)
Полный доступ ко всем ресурсам системы:
- Управление пользователями (чтение, создание, редактирование, удаление)
- Управление пациентами, врачами
- Управление медицинскими картами
- Управление комнатами
- Одобрение заявок на пребывание
- Управление сеансами и процедурами
- Управление рецептами
- Управление шкафчиками

### DOCTOR (Врач)
Доступ к медицинским данным и управлению пациентами:
- Просмотр информации о пациентах
- Чтение и редактирование медицинских карт
- Просмотр заявок на пребывание
- Создание и просмотр сеансов
- Создание и просмотр рецептов
- Просмотр информации о комнатах

### PATIENT (Пациент)
Доступ к собственным данным:
- Просмотр собственной медицинской карты
- Создание и просмотр заявок на пребывание
- Просмотр назначенных сеансов
- Просмотр рецептов
- Просмотр информации о комнате
- Просмотр информации о шкафчике

## Разрешения (Permissions)

### Пользователи
- `user:read` - чтение данных пользователей
- `user:write` - создание и редактирование пользователей
- `user:delete` - удаление пользователей

### Пациенты
- `patient:read` - просмотр данных пациентов
- `patient:write` - создание и редактирование данных пациентов
- `patient:delete` - удаление пациентов

### Врачи
- `doctor:read` - просмотр данных врачей
- `doctor:write` - создание и редактирование данных врачей
- `doctor:delete` - удаление врачей

### Медицинские карты
- `medical_card:read` - просмотр медицинских карт
- `medical_card:write` - редактирование медицинских карт

### Комнаты
- `room:read` - просмотр информации о комнатах
- `room:write` - управление комнатами

### Заявки на пребывание
- `stay_request:read` - просмотр заявок
- `stay_request:write` - создание заявок
- `stay_request:approve` - одобрение/отклонение заявок

### Сеансы
- `session:read` - просмотр сеансов
- `session:write` - создание и редактирование сеансов
- `session:delete` - удаление сеансов

### Рецепты
- `prescription:read` - просмотр рецептов
- `prescription:write` - создание рецептов

### Шкафчики
- `locker:read` - просмотр информации о шкафчиках
- `locker:write` - управление шкафчиками

## API Endpoints

### Аутентификация (`/auth/**`)
- `POST /auth/login` - вход в систему (доступен всем)
- `POST /auth/register` - регистрация (доступен всем)

### Администратор (`/admin/**`)
Требуется роль ADMIN:
- `GET /admin/users` - список всех пользователей
- `GET /admin/users/{id}` - получить пользователя по ID
- `PUT /admin/users/{id}` - обновить пользователя
- `DELETE /admin/users/{id}` - удалить пользователя
- `POST /admin/users/{id}/lock` - заблокировать пользователя
- `POST /admin/users/{id}/unlock` - разблокировать пользователя
- `POST /admin/users/{id}/disable` - отключить учетную запись
- `POST /admin/users/{id}/enable` - включить учетную запись

### Врач (`/doctor/**`)
Требуется роль DOCTOR или ADMIN:
- `GET /doctor/dashboard` - панель врача
- `GET /doctor/patients` - список пациентов
- `GET /doctor/medical-cards/{id}` - получить медицинскую карту
- `POST /doctor/medical-cards/{id}` - обновить медицинскую карту
- `GET /doctor/sessions` - список сеансов
- `POST /doctor/sessions` - создать сеанс
- `POST /doctor/prescriptions` - создать рецепт
- `GET /doctor/prescriptions/{id}` - получить рецепт

### Пациент (`/patient/**`)
Требуется роль PATIENT, DOCTOR или ADMIN:
- `GET /patient/dashboard` - панель пациента
- `GET /patient/medical-card` - получить свою медицинскую карту
- `GET /patient/stay-requests` - список заявок на пребывание
- `POST /patient/stay-requests` - создать заявку
- `GET /patient/sessions` - список сеансов
- `GET /patient/prescriptions` - список рецептов
- `GET /patient/room` - информация о комнате
- `GET /patient/locker` - информация о шкафчике

## Использование аннотаций безопасности

### @PreAuthorize с ролями
```java
@PreAuthorize("hasRole('ADMIN')")
public void adminOnlyMethod() { }

@PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
public void doctorOrAdminMethod() { }
```

### @PreAuthorize с разрешениями
```java
@PreAuthorize("hasAuthority('user:write')")
public void canWriteUsers() { }

@PreAuthorize("hasAuthority('medical_card:read')")
public void canReadMedicalCards() { }
```

### Комбинированные проверки
```java
@PreAuthorize("hasRole('DOCTOR') and hasAuthority('prescription:write')")
public void createPrescription() { }
```

## Структура JWT токена

Токен содержит:
- `sub` - email пользователя
- `role` - роль пользователя (ADMIN, DOCTOR, PATIENT)
- `authorities` - список всех разрешений
- `iss` - издатель токена
- `iat` - время создания
- `exp` - время истечения

## Безопасность

1. **Пароли** - хешируются с использованием BCrypt
2. **JWT токены** - используются для аутентификации
3. **Проверка состояния** - учетные записи могут быть заблокированы или отключены
4. **Детализированные разрешения** - каждая роль имеет конкретный набор разрешений
5. **Иерархия доступа** - администраторы имеют доступ ко всем ресурсам

## Примеры использования

### Регистрация нового пользователя
```bash
POST /auth/register
{
  "email": "doctor@example.com",
  "password": "SecurePassword123!",
  "name": "Иван Иванов",
  "role": "DOCTOR"
}
```

### Вход в систему
```bash
POST /auth/login
{
  "email": "doctor@example.com",
  "password": "SecurePassword123!"
}
```

### Получение списка пациентов (врач)
```bash
GET /doctor/patients
Authorization: Bearer {jwt_token}
```

### Блокировка пользователя (администратор)
```bash
POST /admin/users/5/lock
Authorization: Bearer {admin_jwt_token}
```

## Расширение системы

Для добавления нового разрешения:
1. Добавьте константу в `Permission.java`
2. Назначьте разрешение нужным ролям в `Role.java`
3. Используйте `@PreAuthorize` в контроллерах

Для добавления новой роли:
1. Добавьте роль в `Role.java` с набором разрешений
2. Обновите enum `user_role` в базе данных
3. Добавьте соответствующие endpoints

