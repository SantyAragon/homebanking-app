Vue.createApp({
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
        this.navbarFunctions();

        axios.get("http://localhost:8080/api/clients/current")
            .then(data => {
                this.client = data.data
                this.accounts = data.data.accounts
                this.accounts.sort((a, b) => a.id - b.id);


            })


        const urlParams = new URLSearchParams(window.location.search);
        const idAccount = urlParams.get('id');

        axios.get("http://localhost:8080/api/accounts/current/" + idAccount)
            .then(data => {
                console.log(data)
                this.account = data.data;
                this.transactions = data.data.transactions
                console.log(this.account);
                console.log(this.transactions);

                this.sortTransactions();
                this.chart();
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

        sortTransactions() {
            this.transactions.sort((a, b) => a.id - b.id)

            //funcion para ordenar por LocalDateTime.
            // this.transactions.sort((a, b) => new Intl.Collator().compare(a.date, b.date))
        },

        formatearFecha(dateInput) {
            const date = new Date(dateInput)

            return (date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear() + " " +
                date.getHours() + ":" + (date.getMinutes() > 9 ? date.getMinutes() : "0" + date.getMinutes()))
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
            var chart = root.container.children.push(
                am5percent.PieChart.new(root, {
                    startAngle: 160,
                    endAngle: 380
                })
            );

            // Create series
            // https://www.amcharts.com/docs/v5/charts/percent-charts/pie-chart/#Series

            var series1 = chart.series.push(
                am5percent.PieSeries.new(root, {
                    startAngle: 160,
                    endAngle: 380,
                    valueField: "amount",
                    innerRadius: am5.percent(80),
                    categoryField: "transaction"
                })
            );




            series1.ticks.template.set("forceHidden", true);
            series1.labels.template.set("forceHidden", true);


            var label = chart.seriesContainer.children.push(
                am5.Label.new(root, {
                    textAlign: "center",
                    centerY: am5.p100,
                    centerX: am5.p50,
                    text: `[bold fontSize: 1rem] TOTAL SPENDED[/]:\n[bold fontSize:1rem]$ ${this.transactions.filter(transaction=>transaction.type=="DEBITO").map(transaction=>transaction.amount).reduce((a,b)=>a+b,0) } [/]`,
                })
            );
            var data = [];

            this.transactions.forEach(transaction => {
                if (transaction.type == "DEBITO") {
                    let element = {};
                    element = {
                        transaction: transaction.description,
                        amount: transaction.amount,
                    }
                    data.push(element)
                }

            })



            // Set data
            // https://www.amcharts.com/docs/v5/charts/percent-charts/pie-chart/#Setting_data

            series1.data.setAll(data);
        },
    },
    computed: {

    },
}).mount('#app')