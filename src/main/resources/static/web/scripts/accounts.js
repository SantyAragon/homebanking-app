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
                        account.transactions.forEach(transaction => this.allTransactions.push(transaction))
                    })
                    this.allTransactions.sort((a, b) => b.id - a.id)
                },
                chart() {

                    var root = am5.Root.new("chartdiv");


                    // Set themes
                    // https://www.amcharts.com/docs/v5/concepts/themes/
                    root.setThemes([
                        am5themes_Animated.new(root)
                    ]);


                    // Create chart
                    // https://www.amcharts.com/docs/v5/charts/percent-charts/pie-chart/
                    var chart = root.container.children.push(am5percent.PieChart.new(root, {
                        layout: root.verticalLayout,
                        innerRadius: am5.percent(75)
                    }));


                    // Create series
                    // https://www.amcharts.com/docs/v5/charts/percent-charts/pie-chart/#Series
                    var series = chart.series.push(am5percent.PieSeries.new(root, {
                        valueField: "balance",
                        categoryField: "account",
                        alignLabels: false
                    }));

                    series.labels.template.setAll({
                        textType: "circular",
                        centerX: 0,
                        centerY: 0
                    });

                    // Set data
                    // https://www.amcharts.com/docs/v5/charts/percent-charts/pie-chart/#Setting_data


                    let arrayCuentas = [];
                    this.cuentas.forEach(account => {
                        let aux = {}
                        aux = {
                            account: account.number,
                            balance: account.balance
                        }
                        arrayCuentas.push(aux);
                    })

                    series.data.setAll(arrayCuentas);


                    // Play initial series animation
                    // https://www.amcharts.com/docs/v5/concepts/animations/#Animation_of_series
                    series.appear(1000, 100);

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
                    axios.post("http://localhost:8080/api/clients/current/accounts").then(response => {}).then(location.reload())
                },
                applyLoan() {
                    let loan = {
                        id: 1,
                        amount: 10000,
                        payment: 24,
                        targetAccount: 'VIN006'

                    }
                    axios.post("http://localhost:8080/api/loans", loan)

                    },
                },
                computed: {

                },
            }).mount('#app')