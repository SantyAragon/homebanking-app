const app = Vue.createApp({
    data() {
        return {
            message: 'Hello Vue!',
            client: [],
            accounts: [],
            cards: [],
            allTransactions: [],
            arrayCuentas: [],
            loans: [],
            topCryptos: [],
            cryptosIcon: [],
            totalBalance: 0,
        }
    },
    created() {
        axios.get("http://localhost:8080/api/clients/current")
            .then(data => {

                this.client = data.data;
                this.accounts = this.client.accounts.sort((a, b) => a.id - b.id);
                this.loans = data.data.loans
                this.totalBalance = this.accounts.map(account => account.balance).reduce((a, b) => a + b, 0)
                this.cards = this.client.cards
                console.log(this.loans)

                this.takeAllTransaction();
            })

        axios.get("http://localhost:8080/api/cryptos")
            .then(data => {
                this.topCryptos = data.data
            })

    },
    methods: {
        disableAccount(id) {


            Swal.fire({
                    title: 'Disable Account?',
                    showDenyButton: true,
                    // showCancelButton: true,
                    confirmButtonText: 'Accept',
                    denyButtonText: `Cancel`,
                })
                .then((result) => {
                    if (result.isConfirmed) {
                        Swal.fire({
                                input: 'password',
                                inputLabel: 'Enter your password for confirm',
                                inputValue: '',

                                showCancelButton: true,
                                confirmButtonText: 'Confirm',
                            })
                            .then(result => {
                                if (result.isConfirmed) {
                                    console.log(result)
                                    axios.patch('http://localhost:8080/api/clients/current/accounts/disabled', `idAccount=${id}&password=${result.value}`)
                                        .then(response => {
                                            Swal.fire('Account deactivated', '', 'success')
                                                .then(result => {
                                                    window.location.reload()
                                                })
                                        }).catch(error => {
                                            this.error = error.response.data
                                            Swal.fire('Failed deactived account', this.error, 'error')
                                            // .then(result => {
                                            //     window.location.reload()
                                            // })
                                        })
                                }
                            })
                    }
                })
        },
        formatearFecha(dateInput) {
            const date = new Date(dateInput)
            return date.toDateString().slice(3)
        },
        formatearHora(dateInput) {
            const date = new Date(dateInput)
            let minutes = date.getMinutes() > 9 ? date.getMinutes() : "0" + date.getMinutes()
            return date.getHours() + ":" + minutes
        },
        takeAllTransaction() {
            this.client.accounts.forEach(account => {
                account.transactions.forEach(transaction => {

                    Object.defineProperty(transaction, 'origen', {
                        value: account.number,
                        writable: true,
                        enumerable: true,
                        configurable: true
                    })
                    this.allTransactions.push(transaction)
                })
            })
            this.allTransactions.sort((a, b) => b.id - a.id)
        },
        managementAccount() {

        },
        logout() {
            axios.post('/api/logout').then(response => console.log('signed out!!!'))
            window.location.href = './index.html'
        },
        formatearPrecio(price) {

            let priceFloat = parseFloat(price)

            if (priceFloat < 0.001) {
                return priceFloat.toFixed(7)
            }
            if (priceFloat < 1) {
                return priceFloat.toFixed(3)
            } else {
                return priceFloat.toFixed(2)
            }

        },
        createAccount() {
            let inputOptions = {
                'SAVINGS': 'Savings',
                'CHECKING': 'Checking',
            }
            Swal.fire({
                    title: 'Select account type',
                    input: 'radio',
                    inputOptions: inputOptions,
                    inputValidator: (value) => {
                        if (!value) {
                            return 'You need to choose something!'
                        }
                    }
                })
                .then((result) => {
                    if (result.isConfirmed) {
                        axios.post("http://localhost:8080/api/clients/current/accounts", `accountType=${result.value}`)
                            .then(response => {
                                Swal.fire('Create Success', '', 'success')
                                    .then(result => {
                                        window.location.reload()
                                    })
                            }).catch(error => {
                                Swal.fire('Creation Failed', error.response.data, 'error')
                                    .then(result => {
                                        window.location.reload()
                                    })
                            })

                    } else if (result.isDenied) {
                        Swal.fire('Cancel creation account', '', 'error')
                    }
                })
        },
    },
    computed: {

    },
}).mount('#app')