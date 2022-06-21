const app = Vue.createApp({
    data() {
        return {
            message: 'Hello Vue!',
            client: {},
            newPassword: "",
            newPasswordConfirm: "",
            token: "",
            error: "",
        }
    },
    created() {
        const urlParams = new URLSearchParams(window.location.search);
        this.token = urlParams.get('token');

        axios.get(`/api/clients/current/verification?token=${this.token}`)
            .then(response => {
                this.client = response.data
                let loader = document.querySelector('#loader-container')
                loader.classList.add('loader-desactive')
            })
    },
    methods: {
        changePassword() {

            if (this.newPassword == this.newPasswordConfirm) {
                axios.patch('/api/clients/current/password', `newPassword=${this.newPassword}&email=${this.client.email}`)
                    .then(location.href = "./index.html")
            } else {
                this.error = "Please, check that their passwords are the same."
            }


        },
        logout() {
            axios.post('/api/logout').then(response => {
                // console.log('signed out!!!')
            })
            window.location.href = './index.html'
        },

    },
    computed: {

    },
}).mount('#app')