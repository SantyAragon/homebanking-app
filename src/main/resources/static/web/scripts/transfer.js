const app = Vue.createApp({
    data() {
        return {
            message: 'Hello Vue!',
            client: {},
            accounts: [],
            typeTransaction: "",
            originAccountNumber: "Select account",
            targetAccountNumber: "Select account",
            thirdAccount: "",
            description: "",
            amount: 0,
            botonDeshabilitado: true,
            button: null,
            step: "stepZero",
            error: "",
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

    },
    mounted() {
        this.changeStepForm();
    },
    methods: {

        sortAccounts() {
            this.accounts.sort((a, b) => a.id - b.id)

            //funcion para ordenar por LocalDateTime.
            // this.transactions.sort((a, b) => new Intl.Collator().compare(a.date, b.date))
        },
        makeTransaction() {

            Swal.fire({
                title: 'Do you make transaction?',
                showDenyButton: true,
                // showCancelButton: true,
                confirmButtonText: 'Accept',
                denyButtonText: `Cancel`,
            }).then((result) => {

                if (result.isConfirmed) {
                    if (this.originAccountNumber != "Select account" && this.targetAccountNumber != "Select account" && this.amount > 0 && this.description != "") {

                        axios.post('http://localhost:8080/api/transactions', `description=${this.description}&amount=${this.amount}&originAccountNumber=${this.originAccountNumber}&targetAccountNumber=${this.targetAccountNumber}`)
                            .then(response => {
                                Swal.fire('Transaction Success', '', 'success')
                                    .then(result => {
                                        window.location.reload()
                                    })
                            }).catch(error => {
                                console.log(error)
                                this.error = error.response.data
                                Swal.fire('Transaction Failed', this.error, 'error')
                                    .then(result => {
                                        window.location.reload()
                                    })
                            })
                    } else if (this.originAccount != "Select account" && this.thirdAccount != "" && this.amount > 0 && this.description != "") {
                        axios.post('http://localhost:8080/api/transactions', `description=${this.description}&amount=${this.amount}&originAccount=${this.originAccount}&targetAccount=${this.thirdAccount}`)
                            .then(response => {
                                Swal.fire('Transaction Success', '', 'success')
                                    .then(result => {
                                        window.location.reload()
                                    })
                            }).catch(error => {
                                this.error = error.response.data
                                Swal.fire('Transaction Failed', this.error, 'error')
                                    .then(result => {
                                        window.location.reload()
                                    })
                            })
                    }
                } else if (result.isDenied) {
                    Swal.fire('Cancel transaction', '', 'error')
                }
            })
        },
        logout() {
            axios.post('/api/logout').then(response => console.log('signed out!!!'))
            window.location.href = './index.html'
        },
        changeStep() {
            switch (this.step) {
                case 'stepZero':
                    this.step = 'stepOne'
                    break
                case 'stepOne':
                    this.step = 'stepTwo'
                    break
                case 'stepTwo':
                    this.step = 'stepThree'
                    break
                case 'stepThree':
                    this.step = 'stepFour'
                    break

            }
            console.log(this.step)
        },
        changeStepForm() {
            let allForms = document.querySelector('#allSteps')


            console.log(this.step)
            let formZero = allForms.querySelector('#stepZero')
            let formOne = allForms.querySelector('#stepOne')
            let formTwo = allForms.querySelector('#stepTwo')
            let formThree = allForms.querySelector('#stepThree')
            let formFour = allForms.querySelector('#stepFour')

            const hiddenOtherSteps = () => {
                formZero.classList.add('d-none')
                formOne.classList.add('d-none')
                formTwo.classList.add('d-none')
                formThree.classList.add('d-none')
                formFour.classList.add('d-none')
            }

            switch (this.step) {
                case 'stepZero':
                    hiddenOtherSteps()
                    formZero.classList.remove('d-none')
                    break
                case 'stepOne':
                    // formOne.classList.add('slideOut')
                    hiddenOtherSteps()
                    formOne.classList.remove('d-none')
                    break;
                case 'stepTwo':
                    // formTwo.classList.add('slideOut')
                    hiddenOtherSteps()
                    formTwo.classList.remove('d-none')
                    break
                case 'stepThree':
                    // formThree.classList.add('slideOut')
                    hiddenOtherSteps()
                    formThree.classList.remove('d-none')
                    break
                case 'stepFour':
                    // setTimeout(hiddenOtherSteps(), 1000)
                    hiddenOtherSteps()
                    formFour.classList.remove('d-none')
                    break
            }


            // if (this.step == 'stepOne') {
            //     formOne.classList.remove('d-none')
            //     formTwo.classList.add('d-none')
            //     formThree.classList.add('d-none')
            //     formFour.classList.add('d-none')
            // }
            // if (this.step == 'stepTwo') {
            //     formTwo.classList.remove('d-none')
            //     formOne.classList.add('d-none')
            //     formThree.classList.add('d-none')
            //     formFour.classList.add('d-none')
            // }
            // if (this.step == 'stepThree') {
            //     formThree.classList.remove('d-none')
            //     formOne.classList.add('d-none')
            //     formTwo.classList.add('d-none')
            //     formFour.classList.add('d-none')
            // }
            // if (this.step == 'stepFour') {
            //     formFour.classList.remove('d-none')
            //     formOne.classList.add('d-none')
            //     formTwo.classList.add('d-none')
            //     formThree.classList.add('d-none')
            // }
        }


    },
    computed: {

    },
}).mount('#app')