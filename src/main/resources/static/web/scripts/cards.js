const app = Vue.createApp({
    data() {
        return {
            message: 'Hello Vue!',
            client: {},
            cards: [],
            cardsDebit: [],
            cardCredit: [],

        }
    },
    created() {

        axios.get("http://localhost:8080/api/clients/current")
            .then(data => {
                this.client = data.data;
                this.cards = data.data.cards;
                this.sortCards();

                this.cardsDebit = this.cards.filter(card => card.type == 'DEBIT')
                this.cardsCredit = this.cards.filter(card => card.type == 'CREDIT')

                // this.removeHoverCardExpired()
            })

    },
    methods: {

        disabledCard(id) {

            Swal.fire({
                    title: 'Disable card?',
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
                                    axios.patch('http://localhost:8080/api/clients/current/cards/disabled', `idCard=${id}&password=${result.value}`)
                                        .then(response => {
                                            Swal.fire('Card deactivated', '', 'success')
                                                .then(result => {
                                                    window.location.reload()
                                                })
                                        }).catch(error => {
                                            this.error = error.response.data
                                            Swal.fire('Failed deactived card', this.error, 'error')
                                                .then(result => {
                                                    window.location.reload()
                                                })
                                        })
                                }
                            })
                    }
                })


        },
        isActive(card) {
            let now = new Date()
            let dateCard = new Date(card.thruDate)

            if (now > dateCard) {
                console.log('now:' + now, 'card' + card.id + ': ' + dateCard, (now > dateCard))
                return false;
            } else {
                console.log('now:' + now, 'card' + card.id + ':' + dateCard, (now > dateCard))
                return true;
            }

        },
        sortCards() {
            this.cards.sort((a, b) => a.id - b.id)

            //funcion para ordenar por LocalDateTime.
            // this.transactions.sort((a, b) => new Intl.Collator().compare(a.date, b.date))
        },
        formatearFechaCard(dateInput) {
            const date = new Date(dateInput);

            // si mes es menor a 10 le agrego un "0"
            let month = (date.getMonth() + 1) > 9 ? date.getMonth() + 1 : "0" + (date.getMonth() + 1);

            let year = date.getFullYear().toString().slice(-2);
            return month + "/" + year
        },
        logout() {
            axios.post('/api/logout').then(response => console.log('signed out!!!'))
            window.location.href = './index.html'
        },

    },
    computed: {

    },
}).mount('#app')