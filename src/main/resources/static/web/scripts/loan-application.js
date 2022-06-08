const app = Vue.createApp({
    data() {
        return {
            message: 'Hello Vue!',
            loans: [],
            client: {},
            accounts: [],
            error: "",
            typeLoan: "Select Loan type",
            amountLoan: "",
            paymentLoan: 0,
            targetAccount: "Select target account",
        }
    },
    created() {

        axios.get("http://localhost:8080/api/clients/current")
            .then(data => {
                this.client = data.data;

                this.accounts = data.data.accounts
                this.sortAccounts();

                let loader = document.querySelector('#loader-container')
                loader.classList.add('loader-desactive')
            })
        axios.get("http://localhost:8080/api/loans")
            .then(data => {
                this.loans = data.data
                this.loans.sort((a, b) => a.id - b.id)
            })
    },
    mounted() {

    },
    methods: {
        selectLoan(loan) {
            this.typeLoan = loan
            console.log(this.typeLoan)
            return this.typeLoan.name
        },
        requestLoan() {
            let loanApplication = {
                id: this.typeLoan.id,
                amount: this.amountLoan,
                payment: this.paymentLoan,
                targetAccount: this.targetAccount
            }

            console.log(loanApplication)

            axios.post('http://localhost:8080/api/loans', loanApplication, {
                headers: {
                    'content-type': 'application/json'
                }
            }).then(response => console.log(response))
        },
        sortAccounts() {
            this.accounts.sort((a, b) => a.id - b.id)

            //funcion para ordenar por LocalDateTime.
            // this.transactions.sort((a, b) => new Intl.Collator().compare(a.date, b.date))
        },

        logout() {
            axios.post('/api/logout').then(response => console.log('signed out!!!'))
            window.location.href = './index.html'
        },

    },
    computed: {

    },
}).mount('#app')