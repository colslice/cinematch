const express = require('express');
const router = express.Router();
const { createReview, getUserReviews, updateReview, deleteReview } = require('../controllers/reviewController');

router.post('/', createReview);
router.get('/user/:userId', getUserReviews);
router.put('/:id', updateReview);
router.delete('/:id', deleteReview);

module.exports = router;