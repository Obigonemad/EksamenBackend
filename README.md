### Exam monday 04.11.2024 
#### Author Patrick Kempf
#### Semester 3
#### Email : cph-pk217@cphbusiness.dk

TODO
1. opret database
2. Ret DB_NAME i config.properties
3. kør main
4. husk at rydde op i koden
```
1. 
```

```
2. 
```

```
3.3.2 and 3.3.3
 HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 09:26:57 GMT
Content-Type: application/json
Content-Length: 61

Database populated successfully with sample trips and guides.
Response file saved.
> 2024-11-04T102657.200.json

Response code: 200 (OK); Time: 150ms (150 ms); Content length: 61 bytes (61 B)

GET http://localhost:7070/api/trips

HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 09:27:58 GMT
Content-Type: application/json
Content-Length: 771

[
  {
    "id": 1,
    "starttime": [
      2024,
      11,
      5,
      10,
      26,
      57,
      145614000
    ],
    "endtime": [
      2024,
      11,
      6,
      10,
      26,
      57,
      145614000
    ],
    "longitude": 34.0522,
    "latitude": -118.2437,
    "name": "Beach Adventure",
    "price": 299.99,
    "category": "BEACH"
  },
  {
    "id": 2,
    "starttime": [
      2024,
      11,
      7,
      10,
      26,
      57,
      150844000
    ],
    "endtime": [
      2024,
      11,
      8,
      10,
      26,
      57,
      150844000
    ],
    "longitude": 40.7128,
    "latitude": -74.006,
    "name": "City Explorer",
    "price": 399.99,
    "category": "CITY"
  },
  {
    "id": 3,
    "starttime": [
      2024,
      11,
      9,
      10,
      26,
      57,
      153531000
    ],
    "endtime": [
      2024,
      11,
      10,
      10,
      26,
      57,
      153531000
    ],
    "longitude": 47.6062,
    "latitude": -122.3321,
    "name": "Forest Hike",
    "price": 199.99,
    "category": "FOREST"
  },
  {
    "id": 4,
    "starttime": [
      2024,
      11,
      11,
      10,
      26,
      57,
      155186000
    ],
    "endtime": [
      2024,
      11,
      12,
      10,
      26,
      57,
      155186000
    ],
    "longitude": 37.7749,
    "latitude": -122.4194,
    "name": "Mountain Expedition",
    "price": 499.99,
    "category": "SNOW"
  }
]
Response file saved.
> 2024-11-04T102759.200.json

Response code: 200 (OK); Time: 606ms (606 ms); Content length: 771 bytes (771 B)

GET http://localhost:7070/api/trips/1

HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 09:28:36 GMT
Content-Type: application/json
Content-Length: 192

{
  "id": 1,
  "starttime": [
    2024,
    11,
    5,
    10,
    26,
    57,
    145614000
  ],
  "endtime": [
    2024,
    11,
    6,
    10,
    26,
    57,
    145614000
  ],
  "longitude": 34.0522,
  "latitude": -118.2437,
  "name": "Beach Adventure",
  "price": 299.99,
  "category": "BEACH"
}
Response file saved.
> 2024-11-04T102836.200.json

Response code: 200 (OK); Time: 20ms (20 ms); Content length: 192 bytes (192 B)

POST http://localhost:7070/api/trips

HTTP/1.1 201 Created
Date: Mon, 04 Nov 2024 09:28:57 GMT
Content-Type: application/json
Content-Length: 161

{
  "id": 5,
  "starttime": [
    2024,
    12,
    1,
    10,
    0
  ],
  "endtime": [
    2024,
    12,
    2,
    18,
    0
  ],
  "longitude": 34.0522,
  "latitude": -118.2437,
  "name": "Beach Getaway",
  "price": 200.0,
  "category": "BEACH"
}
Response file saved.
> 2024-11-04T102857.201.json

Response code: 201 (Created); Time: 62ms (62 ms); Content length: 161 bytes (161 B)

PUT http://localhost:7070/api/trips/1

HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 09:29:19 GMT
Content-Type: application/json
Content-Length: 132

{
  "id": 1,
  "starttime": null,
  "endtime": null,
  "longitude": 0.0,
  "latitude": 0.0,
  "name": "Updated Beach Getaway",
  "price": 250.0,
  "category": null
}
Response file saved.
> 2024-11-04T102919.200.json

Response code: 200 (OK); Time: 34ms (34 ms); Content length: 132 bytes (132 B)

DELETE http://localhost:7070/api/trips/1

HTTP/1.1 204 No Content
Date: Mon, 04 Nov 2024 09:29:49 GMT
Content-Type: text/plain

<Response body is empty>

Response code: 204 (No Content); Time: 20ms (20 ms); Content length: 0 bytes (0 B)

PUT http://localhost:7070/api/trips/1/guides/1

HTTP/1.1 500 Server Error
Date: Mon, 04 Nov 2024 09:30:05 GMT
Content-Type: application/json
Content-Length: 110

{
  "warning": "Failed to add guide to trip.",
  "time": "2024-11-04T09:30:06.006501800Z",
  "status": "500 Server Error"
}
Response file saved.
> 2024-11-04T103006.500.json

Response code: 500 (Server Error); Time: 22ms (22 ms); Content length: 110 bytes (110 B)

3.3.5
En PUT-anmodning er idempotent, hvilket betyder, at den samme anmodning kan sendes flere gange uden at ændre resultatet efter den første gang.
Det passer til formålet med at opdatere en eksisterende ressource, som i dette tilfælde er turen, ved at tilføje en guide.
En POST-anmodning bruges derimod til at oprette nye ressourcer, hvilket ikke er relevant her. Derfor er PUT mere hensigtsmæssigt for at tydeliggøre intentionen om at ændre turens oplysninger.
```

```
4. 
```

```
5. 
```

```
6. 
```

```
7. 
```

```
8.3  
jeg har implementeret det at man skal være user for at kunne se trips i min kode. 
```

```
the end.