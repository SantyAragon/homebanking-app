Vue.createApp({
    data() {
        return {
            message: 'Hello Vue!',
            client: {},
            cuentas: {},
            arrayCuentas: [],
            loans: [],
            topCryptos: [],
            cryptosIcon: [],
        }
    },
    created() {
        this.navbarFunctions();

        axios.get("http://localhost:8080/api/clients/current")
            .then(data => {

                this.client = data.data;
                this.cuentas = this.client.accounts.sort((a, b) => a.id - b.id);
                this.loans = data.data.loans
                console.log(this.loans)
                this.chart();
            })
        // axios.get("http://localhost:8080/api/cryptos").then(response => {
        //     console.log(response.data.data)
        //     this.topCryptos = response.data.data
        // })
        // axios.get("http://localhost:8080/api/cryptos/img").then(response => {
        //     console.log(response.data.data)
        //     this.cryptosIcon = response.data.data
        // })
        axios.get("http://localhost:8080/api/cryptos")
            .then(data => {
                console.log(data.data)
                this.topCryptos = data.data
            })
    },
    methods: {

        // chart() {


        //     var root = am5.Root.new("chartdiv");

        //     // Set themes
        //     // https://www.amcharts.com/docs/v5/concepts/themes/
        //     root.setThemes([
        //         am5themes_Animated.new(root)
        //     ]);

        //     // Create chart
        //     // https://www.amcharts.com/docs/v5/charts/percent-charts/pie-chart/
        //     var chart = root.container.children.push(
        //         am5percent.PieChart.new(root, {
        //             startAngle: 160,
        //             endAngle: 380
        //         })
        //     );

        //     // Create series
        //     // https://www.amcharts.com/docs/v5/charts/percent-charts/pie-chart/#Series

        //     var series1 = chart.series.push(
        //         am5percent.PieSeries.new(root, {
        //             startAngle: 160,
        //             endAngle: 380,
        //             valueField: "balance",
        //             innerRadius: am5.percent(80),
        //             categoryField: "number"
        //         })
        //     );

        //     series1.ticks.template.set("forceHidden", true);
        //     series1.labels.template.set("forceHidden", true);

        //     var label = chart.seriesContainer.children.push(
        //         am5.Label.new(root, {
        //             textAlign: "center",
        //             centerY: am5.p100,
        //             centerX: am5.p50,
        //             text: `[ fontSize:1rem]TOTAL BALANCE[/]:\n[ fontSize:1rem]$ ${this.cuentas.map(account=> account.balance).reduce((a,b)=>a+b,0)}[/]`
        //         })
        //     );

        //     var data = [];
        //     this.cuentas.forEach(account => {
        //         let aux = {}
        //         aux = {
        //             balance: account.balance,
        //             number: account.number
        //         }
        //         data.push(aux)
        //     })


        //     // Set data
        //     // https://www.amcharts.com/docs/v5/charts/percent-charts/pie-chart/#Setting_data
        //     series1.data.setAll(data);

        // },

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
        logout() {
            axios.post('/api/logout').then(response => console.log('signed out!!!'))
            window.location.href = './index.html'
        },
        formatearPrecio(price) {

            // priceArr = Array.from(price.toString())

            // if (priceArr[0] != 0 && priceArr[1] != 0 && priceArr[2] != 0) {
            //     return price.toFixed(1)
            // }

            // if (priceArr[2] == 0 && priceArr[3] == 0) {

            //     return price.toFixed(7);
            // } else {

            //     return price.toFixed(3)
            // }

            if (price.toString() < 0.001) {
                return price
            }
            if (price.toString() < 1) {
                return price
            } else {
                return price
            }

        }

    },
    computed: {

    },
}).mount('#app')