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
            dateNowInput: "",
        }
    },
    created() {

        axios.get("/api/clients/current")
            .then(data => {
                this.client = data.data
                this.accounts = data.data.accounts
                this.accounts.sort((a, b) => a.id - b.id);

                let loader = document.querySelector('#loader-container')
                loader.classList.add('loader-desactive')
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
        limitInput() {

            let now = new Date();
            var year = now.getFullYear();
            var date = now.getDate();
            var month = now.getMonth(); //viene con valores de 0 al 11
            month = month + 1; //ahora lo tienes de 1 al 12
            if (month < 10) //ahora le agregas un 0 para el formato date
            {
                month = "0" + month;
            } else {
                month = month.toString;
            }

            let maxNow = year + "-" + month + "-" + date
            this.dateNowInput = maxNow
            // console.log(this.dateNowInput)
            return maxNow

        },
        formatDate(dateInput) {
            const date = new Date(dateInput)

            return (date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear() + " " +
                date.getHours() + ":" + (date.getMinutes() > 9 ? date.getMinutes() : "0" + date.getMinutes()))
        },
        formatTime(dateInput) {
            const date = new Date(dateInput)
            let minutes = date.getMinutes() > 9 ? date.getMinutes() : "0" + date.getMinutes()
            return date.getHours() + ":" + minutes
        },
        logout() {
            axios.post('/api/logout')
                .then(response => {
                    // console.log('signed out!!!')
                })
            window.location.href = './index.html'
        },
        generatePDF() {
            let numberAndDateDTO = {
                numberAccount: this.numberAccount,
                since: this.since,
                until: this.until
            }

            Swal.fire({
                title: 'Do you want to download it?',
                showDenyButton: true,
                // showCancelButton: true,
                confirmButtonText: 'Accept',
                denyButtonText: `Cancel`,
            }).then((result) => {

                if (result.isConfirmed) {

                    axios.post('/api/transactions/generate', numberAndDateDTO, {
                            'responseType': 'blob'
                        })
                        .then(response => {
                            let url = window.URL.createObjectURL(new Blob([response.data]))
                            let link = document.createElement("a")
                            link.href = url;
                            link.setAttribute("download", `DanoBank_${this.numberAccount}_${this.since}-${this.until}.pdf`)
                            document.body.appendChild(link)
                            link.click()
                        })
                        .catch(error => {

                            Swal.fire('Download Failed', '', 'error')
                                .then(result => {
                                    window.location.reload()
                                })
                        })
                } else if (result.isDenied) {
                    Swal.fire('Cancel download', '', 'error')
                }
            })
        }

    },
    computed: {

    },
}).mount('#app')