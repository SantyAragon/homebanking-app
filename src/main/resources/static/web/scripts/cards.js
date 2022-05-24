Vue.createApp({
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
        this.navbarFunctions();

        axios.get("http://localhost:8080/api/clients/current")
            .then(data => {
                this.client = data.data;
                this.cards = data.data.cards;
                this.sortCards();

                this.cardsDebit = this.cards.filter(card => card.type == 'DEBIT')
                this.cardsCredit = this.cards.filter(card => card.type == 'CREDIT')

                console.log(this.cardsCredit)
                console.log(this.cardsDebit)

            })

    },
    methods: {

        navbarFunctions() {
            document.addEventListener("DOMContentLoaded", function (event) {
                const showNavbar = (toggleId, navId, bodyId, headerId) => {
                    const toggle = document.getElementById(toggleId),
                        nav = document.getElementById(navId),
                        bodypd = document.getElementById(bodyId),
                        headerpd = document.getElementById(headerId)

                    if (toggle && nav && bodypd && headerpd) {
                        toggle.addEventListener('click', () => {
                            nav.classList.toggle('show')
                            toggle.classList.toggle('bx-x')
                            bodypd.classList.toggle('body-pd')
                            headerpd.classList.toggle('body-pd')
                        })
                    }
                }

                showNavbar('header-toggle', 'nav-bar', 'body-pd', 'header')

                const linkColor = document.querySelectorAll('.nav_link')

                function colorLink() {
                    if (linkColor) {
                        linkColor.forEach(l => l.classList.remove('active'))
                        this.classList.add('active')
                    }
                }
                linkColor.forEach(l => l.addEventListener('click', colorLink))

            });
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

    },
    computed: {

    },
}).mount('#app')