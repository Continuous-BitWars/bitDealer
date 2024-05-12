Vue.createApp({
    data() {
        return {
            isDarkMode: false
        }
    },

    created() {
        // Update theme (light / dark)
        const themePreference = localStorage.getItem('darkMode')

        if(themePreference) {
            this.isDarkMode = JSON.parse(themePreference);
            this.applyTheme();
        }
    },

    beforeDestroy() {
    },

    mounted() {
        if (this.isDarkMode) {
            document.getElementById("switch").checked = true;
        }
    },

    methods: {

        toggleDarkMode() {
            this.isDarkMode = !this.isDarkMode;

            this.applyTheme();
            localStorage.setItem('darkMode', JSON.stringify(this.isDarkMode));
        },
        applyTheme() {
            if(this.isDarkMode) {
                document.body.classList.add('dark-mode');
            } else {
                document.body.classList.remove('dark-mode');
            }
        }
    }
}).mount('#app')