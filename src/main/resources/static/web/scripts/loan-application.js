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
            targetAccount: "",
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

    methods: {
        isDisabled(loan) {
            let estate = false;

            this.client.loans.forEach(clientLoan => {
                if (clientLoan.idLoan == loan.id) {
                    estate = true;
                }
            })
            // console.log(estate)
            return estate

        },
        selectLoan(loan) {
            this.typeLoan = loan
            console.log(this.typeLoan)
            return this.typeLoan.name
        },
        resetTypeLoan() {
            this.amountLoan = ""
            this.paymentLoan = 0

        },
        requestLoan() {

            let loanApplication = {
                id: this.typeLoan.id,
                amount: this.amountLoan,
                payment: this.paymentLoan,
                targetAccount: this.targetAccount
            }
            console.log(loanApplication)


            Swal.fire({
                    title: 'Do you make transaction?',
                    showDenyButton: true,
                    // showCancelButton: true,
                    confirmButtonText: 'Accept',
                    denyButtonText: `Cancel`,
                })
                .then((result) => {
                    if (result.isConfirmed) {
                        axios.post('http://localhost:8080/api/loans', loanApplication, {
                            headers: {
                                'content-type': 'application/json'
                            }
                        }).then(response => {
                            Swal.fire('Loan approved', '', 'success')
                                .then(result => {
                                    window.location.reload()
                                })
                        }).catch(error => {
                            this.error = error.response.data
                            Swal.fire('Loan request Failed', this.error, 'error')
                                .then(result => {
                                    window.location.reload()
                                })
                        })
                    }
                })
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