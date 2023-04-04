# day22workshop
Basic Spring-boot application to CRUD RSVP responses into mySQL database

1) Get all RSVPs: hostname:port/api/rsvps
2) Get RSVP by name: hostname:port/api/rsvp?q=name
3) Post RSVP (Accept: application/json) : hostname:port/api/rsvp
4) Update RSVP by email (Accept: application/json) : hostname:port/api/rsvp/email

Sample json:
{
"name": "John3 Doe",
"email": "john11.doe@example.com",
"phone": "999-1234",
"confirmation_date": "2023-03-03T08:01:54z",
"comments": "Looking forward to the event!"
}
