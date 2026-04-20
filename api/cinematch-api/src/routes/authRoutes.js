const express = require('express');
const router = express.Router();
const { signup, login } = require('../controllers/authController');

// Map endpoints to controller functions
router.post('/signup', signup);
router.post('/login', login);

module.exports = router;