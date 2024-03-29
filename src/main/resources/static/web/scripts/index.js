const app = Vue.createApp({
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
            errorRegister: "",

            register: false,
            logged: false
        }
    },
    created() {
        this.isAuthenticated()
        let loader = document.querySelector('#loader-container')
        loader.classList.add('loader-desactive')
    },
    methods: {
        login() {

            axios.post('/api/login', `email=${this.email}&password=${this.password}`)
                .then(response => {
                    // window.location.href = '../web/accounts.html'
                    axios.get("/api/admin")
                        .then(response => {


                            if (response.data == "is admin") {
                                Swal.fire({
                                    title: 'Hello admin!',
                                    showDenyButton: true,
                                    // showCancelButton: true,
                                    confirmButtonText: 'Go admin management',
                                    denyButtonText: `Go client view`,
                                }).then((result) => {
                                    /* Read more about isConfirmed, isDenied below */
                                    if (result.isConfirmed) {
                                        window.location.href = "../web/admin.html"
                                    } else if (result.isDenied) {
                                        window.location.href = "../web/accounts.html"
                                    }
                                })
                            } else if (response.data == "is client") {
                                window.location.href = '../web/accounts.html'
                            }

                        })
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
            axios.post('/api/logout').then(response => {
                // console.log('signed out!!!')
            })
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
        toggleRegister() {
            if (this.register) {
                // console.log("xd")
                this.register = false
            } else {
                // console.log("dx")
                this.register = true;
            }
        },
        isAuthenticated() {
            axios.get('/api/authenticated')
                .then(response => {
                    if (response.data == "authenticated") {
                        this.logged = true;
                        // console.log("probando equisde")
                    } else if (response.data == "not authenticated") {
                        this.logged = false
                        // console.log("probando xd");
                    }

                })
        }
    },

    computed: {

    },
}).mount('#app')