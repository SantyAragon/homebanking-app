const app = Vue.createApp({
    data() {
        return {
            message: 'Hello Vue!',
            client: {},
            cards: [],
            cardsDebit: [],
            cardCredit: [],
            colorCard: "SILVER",
            cardType: "CREDIT",

        }
    },
    created() {

        axios.get("/api/clients/current")
            .then(data => {
                this.client = data.data;
                this.cards = data.data.cards;
                this.sortCards();

                this.cardsDebit = this.cards.filter(card => card.type == 'DEBIT')
                this.cardsCredit = this.cards.filter(card => card.type == 'CREDIT')

                // console.log(this.cardsCredit)
                // console.log(this.cardsDebit)

            })

    },
    methods: {
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

        createCard() {
            axios.post('/api/clients/current/cards', `colorCard=${this.colorCard}&cardType=${this.cardType}`, {
                    headers: {
                        'content-type': 'application/x-www-form-urlencoded'
                    }
                }).then(response => {
                    window.location.href = "./cards.html";
                })
                .catch(error => {
                    // console.log(error)
                })
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