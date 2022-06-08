const app = Vue.createApp({
    data() {
        return {
            message: 'Hello Vue!',
            client: {},
            account: {},
            accounts: [],
            transactions: {},
        }
    },
    created() {


        axios.get("http://localhost:8080/api/clients/current")
            .then(data => {
                this.client = data.data
                this.accounts = data.data.accounts
                this.accounts.sort((a, b) => a.id - b.id);


            })


        const urlParams = new URLSearchParams(window.location.search);
        const idAccount = urlParams.get('id');

        axios.get("http://localhost:8080/api/clients/current/accounts/" + idAccount)
            .then(data => {
                console.log(data)
                this.account = data.data;
                this.transactions = data.data.transactions
                console.log(this.account);
                console.log(this.transactions);

                this.sortTransactions();

            })

    },
    methods: {

        sortTransactions() {
            this.transactions.sort((a, b) => b.id - a.id)

            //funcion para ordenar por LocalDateTime.
            // this.transactions.sort((a, b) => new Intl.Collator().compare(a.date, b.date))
        },

        formatearFecha(dateInput) {
            const date = new Date(dateInput)

            return (date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear() + " " +
                date.getHours() + ":" + (date.getMinutes() > 9 ? date.getMinutes() : "0" + date.getMinutes()))
        },

        logout() {
            axios.post('/api/logout').then(response => console.log('signed out!!!'))
            window.location.href = './index.html'
        },
    },
    computed: {

    },
}).mount('#app')