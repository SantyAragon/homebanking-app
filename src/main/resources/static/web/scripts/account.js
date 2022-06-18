const app = Vue.createApp({
    data() {
        return {
            message: 'Hello Vue!',
            client: {},
            account: {},
            accounts: [],
            transactions: {},
            numberAccount: "",
            until: null,
            since: null,
        }
    },
    created() {


        axios.get("/api/clients/current")
            .then(data => {
                this.client = data.data
                this.accounts = data.data.accounts
                this.accounts.sort((a, b) => a.id - b.id);


            })


        const urlParams = new URLSearchParams(window.location.search);
        const idAccount = urlParams.get('id');

        axios.get("/api/clients/current/accounts/" + idAccount)
            .then(data => {

                this.account = data.data;
                this.transactions = data.data.transactions
                this.numberAccount = this.account.number


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
        formatearHora(dateInput) {
            const date = new Date(dateInput)
            let minutes = date.getMinutes() > 9 ? date.getMinutes() : "0" + date.getMinutes()
            return date.getHours() + ":" + minutes
        },
        logout() {
            axios.post('/api/logout').then(response => console.log('signed out!!!'))
            window.location.href = './index.html'
        },
        generatePDF() {
            let numberAndDateDTO = {
                numberAccount: this.numberAccount,
                since: this.since,
                until: this.until
            }

            // axios({
            //         url: '/api/transactions/generate',
            //         method: 'POST',
            //         responseType: "blob",
            //         data: {
            //             numberAccount: this.numberAccount,
            //             since: this.since,
            //             until: this.until
            //         },
            //     }).then(response => {
            //         let url = window.URL.createObjectURL(new Blob([response.data]))
            //         let link = document.createElement("a")
            //         console.log(response);
            //         link.href = url;
            //         link.setAttribute("download", `DanoBank_${this.numberAccount}_${this.since}-${this.until}.pdf`)
            //         document.body.appendChild(link)
            //         link.click()
            //     })
            //     .catch(error => {
            //         console.log(error)
            //     })

            axios.post('/api/transactions/generate', numberAndDateDTO, {
                    'responseType': 'blob'
                })
                .then(response => {
                    let url = window.URL.createObjectURL(new Blob([response.data]))
                    let link = document.createElement("a")
                    console.log(response);
                    link.href = url;
                    link.setAttribute("download", `DanoBank_${this.numberAccount}_${this.since}-${this.until}.pdf`)
                    document.body.appendChild(link)
                    link.click()
                })
                .catch(error => {
                    console.log(error)
                })
        }
    },
    computed: {

    },
}).mount('#app')