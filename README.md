# siteminder-mail
A fault tolerant email service for customers.

##### Request format:

```javascript
{
  "from": "from@example.com",
  "to": [
    "to-01@example.com",
    "to-02@example.com"
  ],
  "cc": [
    "cc@example.com"
  ],
  "bcc": [
    "bcc@example.com"
  ],  
  "subject": "<Email Subject goes here ...>",
  "message": "<Email Body goes here ... >"
}
