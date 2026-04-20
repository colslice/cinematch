const express = require('express');
const router = express.Router();
const { addToWatchList, getUserWatchList, updateWatchListStatus, removeFromWatchList } = require('../controllers/watchlistController');

router.post('/', addToWatchList);
router.get('/user/:userId', getUserWatchList);
router.put('/:id', updateWatchListStatus);
router.delete('/:id', removeFromWatchList);

module.exports = router;