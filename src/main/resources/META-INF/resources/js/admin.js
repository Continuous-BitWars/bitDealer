Vue.createApp({
    data() {
        return {
            games: [],           // Store the retrieved game data
            showDialog: false,   // Flag to show/hide the create game dialog
            newGameName: '',     // Store the new game name
            selectedGame: null,  // Store the selected game details
            isDarkMode: false,
            newPlayerId: '',
            newPlayerName: '',
            newPlayerURL: '',
            newColor: '#FFFFFF',
        };
    },
    created() {
        const themePreference = localStorage.getItem('darkMode')

        if (themePreference) {
            this.isDarkMode = JSON.parse(themePreference);
            this.applyTheme();
        }
    },
    mounted() {
        // Fetch data from the /games endpoint using a GET request
        this.loadGames();

        if (this.isDarkMode) {
            document.getElementById("switch").checked = true;
        }
    },
    methods: {
        loadGames() {
            fetch('/games')
                .then(response => response.json())
                .then(data => {
                    this.games = data; // Update the games data in Vue
                    if (this.selectedGame) {
                        this.selectedGame = this.games.find(game => game.id === this.selectedGame.id);
                    }
                })
                .catch(error => {
                    console.error('Error fetching data:', error);
                });
        },
        showCreateGameDialog() {
            this.showDialog = true;
        },
        cancelCreateGame() {
            this.showDialog = false;
            this.newGameName = '';
        },
        createGame() {
            fetch('/games', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    "name": this.newGameName,
                }),
            })
                .then(response => {
                    if (response.ok) {
                        this.loadGames(); // Reload the game list on success
                        this.showDialog = false;
                        this.newGameName = '';
                    } else {
                        console.error('Error creating game:', response.statusText);
                    }
                })
                .catch(error => {
                    console.error('Error creating game:', error);
                });
        },
        selectGame(game) {
            this.selectedGame = game;
            this.fetchGameById(this.selectedGame.id);
        },
        fetchGameById(gameId) {
            fetch(`/games/${gameId}`, {
                method: 'GET'
            })
                .then(response => response.json())
                .then(response => {
                    if (this.selectedGame.id === gameId) {
                        this.selectedGame = response;
                        this.loadGames()
                    }
                })
                .catch(error => {
                    console.error('Error fetching data:', error);
                });
        },
        startGame(gameId) {
            fetch(`/games/${gameId}/running`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    "time_between_ticks": this.selectedGame.game_options.time_between_ticks,
                }),
            })
                .then(response => {
                    this.fetchGameById(gameId);
                })
                .catch(error => {
                    console.error('Error fetching data:', error);
                });

        },
        stopGame(gameId) {
            fetch(`/games/${gameId}/running`, {
                method: 'DELETE',
            })
                .then(response => {
                    this.fetchGameById(gameId);
                })
                .catch(error => {
                    console.error('Error fetching data:', error);
                });

        },
        addNewPlayer() {
            if (this.selectedGame) {
                const gameId = this.selectedGame.id;

                // Send a POST request to /games/manage/{gameId}/players with query parameters
                fetch(`/games/${gameId}/players`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        "id": this.newPlayerId,
                        "name": this.newPlayerName,
                        "provider_url": this.newPlayerURL,
                        "color": this.newColor
                    }),
                })
                    .then(response => {
                        if (response.ok) {
                            // Reload the selected game to update the team list
                            this.fetchGameById(gameId);
                            this.newPlayerId = '';
                            this.newPlayerName = '';
                            this.newPlayerURL = '';
                        } else {
                            console.error('Error adding Player:', response.statusText);
                        }
                    })
                    .catch(error => {
                        console.error('Error adding Player:', error);
                    });
            }
        },
        removePlayer(playerId) {
            if (this.selectedGame) {
                const gameId = this.selectedGame.id;
                fetch(`/games/${gameId}/players/${playerId}`, {
                    method: 'DELETE',
                })
                    .then(response => {
                        this.fetchGameById(gameId);
                    })
                    .catch(error => {
                        console.error('Error remove Player:', error);
                    });
            }
        },
        removeGame(gameId) {
            fetch(`/games/${gameId}`, {
                method: 'DELETE',
            })
                .then(response => {
                    if (response.ok) {
                        this.selectedGame = undefined;
                        this.loadGames();
                    } else {
                        console.error('Error removing game:', response.statusText);
                    }
                })
                .catch(error => {
                    console.error('Error removing game:', error);
                });
        },
        toggleDarkMode() {
            this.isDarkMode = !this.isDarkMode;

            this.applyTheme();
            localStorage.setItem('darkMode', JSON.stringify(this.isDarkMode));
        },
        applyTheme() {
            if (this.isDarkMode) {
                document.body.classList.add('dark-mode');
            } else {
                document.body.classList.remove('dark-mode');
            }
        }
    },
}).mount('#app')