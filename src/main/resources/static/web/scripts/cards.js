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
        logout() {
            axios.post('/api/logout').then(response => console.log('signed out!!!'))
            window.location.href = './index.html'
        },

    },
    computed: {

    },
}).mount('#app')