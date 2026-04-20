const express = require('express');
const router = express.Router();
const { addService, removeService, addGenre, removeGenre, updateServices, updateGenres, getServices, getGenres, toggleField } = require('../controllers/userController');

// Services
router.post('/:id/services/add', addService);
router.delete('/:id/services/remove', removeService);
router.get('/:id/services', getServices);
router.put('/:id/services', updateServices);

// Genres
router.get('/:id/genres', getGenres);
router.put('/:id/genres', updateGenres);
router.post('/:id/genres/add', addGenre);
router.delete('/:id/genres/remove', removeGenre);

// Toggle fields
router.patch('/:id/toggle/:field', toggleField);

module.exports = router;
