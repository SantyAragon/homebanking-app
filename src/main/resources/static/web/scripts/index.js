Vue.createApp({
    data() {
        return {
            message: 'Hello Vue!',
            email: "",
            password: "",
            firstNameSign: "",
            lastNameSign: "",
            emailSign: "",
            passwordSign: "",
            errorLogin: "",
            errorRegister: ""

        }
    },
    created() {

    },
    methods: {
        login() {

            axios.post('/api/login', `email=${this.email}&password=${this.password}`, {
                    headers: {
                        'content-type': 'application/x-www-form-urlencoded'
                    }
                }).then(response => {
                    window.location.href = '../web/accounts.html'
                })
                .catch(error => {
                    this.errorLogin = "User or password incorrect."
                });

        },
        signup() {
            axios.post('/api/clients', `firstName=${this.firstNameSign}&lastName=${this.lastNameSign}&email=${this.emailSign}&password=${this.passwordSign}`)
                .then(response => {

                    this.email = this.emailSign
                    this.password = this.passwordSign
                    this.login()
                })
                .catch(error => {
                    if (error.response.data == 'Missing data') {
                        this.errorRegister = "Missing Data";
                    }
                    if (error.response.data == 'Email already in use') {
                        this.errorRegister = "Email already in use";
                    }
                })
        },
        logout() {
            axios.post('/api/logout').then(response => console.log('signed out!!!'))
            window.location.href = '/index.html'
        },
        signupButton() {
            let userForms = document.getElementById('user_options-forms')

            userForms.classList.remove('bounceRight')
            userForms.classList.add('bounceLeft')
        },
        loginButton() {
            let userForms = document.getElementById('user_options-forms')
            userForms.classList.remove('bounceLeft')
            userForms.classList.add('bounceRight')
        },


    },


    computed: {

    },
}).mount('#app')