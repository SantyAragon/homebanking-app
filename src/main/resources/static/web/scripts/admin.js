const app = Vue.createApp({
    data() {
        return {
            message: 'Hello Vue!',
            client: {},

            nameLoan: "",
            increaseLoan: null,
            maxAmountLoan: null,
            paymentsLoan: "",
            arrayPayments: []

        }
    },
    created() {

        axios.get("/api/clients/current")
            .then(data => {
                this.client = data.data;
                let loader = document.querySelector('#loader-container')
                loader.classList.add('loader-desactive')
            })

    },
    mounted() {

    },
    methods: {

        logout() {
            axios.post('/api/logout').then(response => {
                // console.log('signed out!!!')
            })
            window.location.href = './index.html'
        },
        createNewLoan() {
            // let regExp = " ^ [0 - 9] * "
            if (this.paymentsLoan.length < 1) {
                this.paymentsLoan = null
            }
            this.arrayPayments = Array.from(this.paymentsLoan.split(","))


            axios.post('/api/loans/create', `nameLoan=${this.nameLoan}&percentIncrease=${this.increaseLoan}&maxAmount=${this.maxAmountLoan}&payments=${this.paymentsLoan}`)
                .then(response => window.location.href = "./loan-application.html")
                .catch(error => {
                    error.response
                })
        }
    },
    computed: {

    },
}).mount('#app')