Vue.createApp({
    data() {
        return {
            message: 'Hello Vue!',
            clients: [],
            client: {},
            urlclienteSeleccionado: "",
            clienteSeleccionado: {},
            urlCuentaSeleccionada: "",
            contenidoJson: [],

            firstName: "",
            lastName: "",
            email: "",
            firstNameEdit: "",
            lastNameEdit: "",
            emailEdit: "",
        }
    },

    created() {
        axios.get('/api/clients')
            .then(datos => {
                this.clients = datos.data
                // this.clients = datos.data._embedded.clients
                this.contenidoJson = datos.data
                let loader = document.querySelector('#loader-container')
                loader.classList.add('loader-desactive')
            })
    },
    methods: {
        agregarATabla() {
            this.client = {
                firstName: this.firstName,
                lastName: this.lastName,
                email: this.email,
            }
            if (this.firstName != "" && this.lastName != "" && this.email != "" && this.email.includes("@" && ".")) {
                axios.post('/rest/clients', this.client)
            }

        },
        capturarCliente(cliente) {
            this.clienteSeleccionado = cliente;
            this.urlclienteSeleccionado = "/rest/clients/" + cliente.id;
        },
        editarCliente(url) {
            this.firstNameEdit = document.querySelector("#firstNameEdit").value
            this.lastNameEdit = document.querySelector("#lastNameEdit").value
            this.emailEdit = document.querySelector("#emailEdit").value

            this.client = {
                firstName: this.firstNameEdit,
                lastName: this.lastNameEdit,
                email: this.emailEdit,
            }
            axios.patch(url, this.client)
                .then(location.reload())
        },
        removerCliente(url) {
            this.clienteSeleccionado.accounts.forEach(account => {
                // console.log(account.transactions)
                axios.delete("/rest/accounts/" + account.id)
            });

            function saludos() {
                axios.delete(url)
                // console.log("nos vemos pa, eliminado ")

                Swal.fire({
                    icon: 'success',
                    title: 'Cuentas borradas éxitosamente',
                }).then((result) => {
                    if (result.isConfirmed) {
                        location.reload()
                    }
                })
            }

            setTimeout(saludos, 100);
        },
        editarCuenta(cuenta) {
            this.urlCuentaSeleccionada = "/rest/accounts/" + cuenta.id;

            Swal.fire({
                    input: 'number',
                    inputLabel: 'Enter the new balance',
                    inputValue: cuenta.balance,

                    showCancelButton: true,
                    confirmButtonText: 'Edit',
                })
                .then(result => {
                    if (result.isConfirmed) {

                        axios.patch(this.urlCuentaSeleccionada, {
                            balance: result.value
                        })

                        Swal.fire('Saved!', '', 'success').then((result) => {
                            if (result.isConfirmed) {
                                location.reload()
                            }
                        })
                    }
                })
        },
    },
    computed: {},
}).mount('#app')