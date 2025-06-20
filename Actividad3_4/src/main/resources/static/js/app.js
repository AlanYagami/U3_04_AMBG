// Configuración de la API
const API_BASE_URL = "http://localhost:8080/api"

// Variables globales
let sedes = []
let clientes = []
let almacenes = []
let almacenesDisponibles = []

// Variable global para controlar el modo de edición
let modoEdicion = {
  activo: false,
  tipo: null,
  id: null,
}

// Inicialización de la aplicación
document.addEventListener("DOMContentLoaded", () => {
  initializeApp()
  setupEventListeners()
})

// Inicializar la aplicación
async function initializeApp() {
  try {
    await loadAllData()
    updateDashboard()
    populateSelects()
  } catch (error) {
    console.error("Error al inicializar la aplicación:", error)
    showModal("Error", "Error al cargar los datos iniciales", "error")
  }
}

// Configurar event listeners
function setupEventListeners() {
  // Formularios
  document.getElementById("sede-form").addEventListener("submit", handleSedeSubmit)
  document.getElementById("almacen-form").addEventListener("submit", handleAlmacenSubmit)
  document.getElementById("cliente-form").addEventListener("submit", handleClienteSubmit)
  document.getElementById("transaccion-form").addEventListener("submit", handleTransaccionSubmit)

  // Modal
  document.querySelector(".close").addEventListener("click", closeModal)
  window.addEventListener("click", (event) => {
    const modal = document.getElementById("modal")
    if (event.target === modal) {
      closeModal()
    }
  })
}

// Funciones de navegación
function showSection(sectionName) {
  // Ocultar todas las secciones
  document.querySelectorAll(".section").forEach((section) => {
    section.classList.remove("active")
  })

  // Remover clase active de todos los botones
  document.querySelectorAll(".nav-btn").forEach((btn) => {
    btn.classList.remove("active")
  })

  // Mostrar la sección seleccionada
  document.getElementById(sectionName).classList.add("active")

  // Activar el botón correspondiente
  event.target.classList.add("active")

  // Cargar datos específicos de la sección
  switch (sectionName) {
    case "dashboard":
      updateDashboard()
      break
    case "sedes":
      loadSedes()
      break
    case "almacenes":
      loadAlmacenes()
      break
    case "clientes":
      loadClientes()
      break
    case "transacciones":
      loadTransacciones()
      break
  }
}

// Funciones de carga de datos
async function loadAllData() {
  try {
    const [sedesResponse, clientesResponse, almacenesResponse] = await Promise.all([
      fetch(`${API_BASE_URL}/sedes`),
      fetch(`${API_BASE_URL}/clientes`),
      fetch(`${API_BASE_URL}/almacenes`),
    ])

    sedes = await sedesResponse.json()
    clientes = await clientesResponse.json()
    almacenes = await almacenesResponse.json()

    // Cargar almacenes disponibles
    const disponiblesResponse = await fetch(`${API_BASE_URL}/almacenes/disponibles`)
    almacenesDisponibles = await disponiblesResponse.json()
  } catch (error) {
    console.error("Error al cargar datos:", error)
    throw error
  }
}

async function loadSedes() {
  try {
    const response = await fetch(`${API_BASE_URL}/sedes`)
    sedes = await response.json()
    displaySedes()
    populateSedeSelect()
  } catch (error) {
    console.error("Error al cargar sedes:", error)
    showModal("Error", "Error al cargar las sedes", "error")
  }
}

async function loadClientes() {
  try {
    const response = await fetch(`${API_BASE_URL}/clientes`)
    clientes = await response.json()
    displayClientes()
    populateClienteSelect()
  } catch (error) {
    console.error("Error al cargar clientes:", error)
    showModal("Error", "Error al cargar los clientes", "error")
  }
}

async function loadAlmacenes() {
  try {
    const response = await fetch(`${API_BASE_URL}/almacenes`)
    almacenes = await response.json()
    displayAlmacenes(almacenes)
  } catch (error) {
    console.error("Error al cargar almacenes:", error)
    showModal("Error", "Error al cargar los almacenes", "error")
  }
}

async function loadTransacciones() {
  try {
    const [disponiblesResponse, ocupadosResponse] = await Promise.all([
      fetch(`${API_BASE_URL}/almacenes/disponibles`),
      fetch(`${API_BASE_URL}/almacenes/ocupados`),
    ])

    almacenesDisponibles = await disponiblesResponse.json()
    const almacenesOcupados = await ocupadosResponse.json()

    populateAlmacenSelect()
    displayTransacciones(almacenesOcupados)
  } catch (error) {
    console.error("Error al cargar transacciones:", error)
    showModal("Error", "Error al cargar las transacciones", "error")
  }
}

// Funciones de manejo de formularios
async function handleSedeSubmit(event) {
  event.preventDefault()

  const formData = {
    estado: document.getElementById("sede-estado").value,
    municipio: document.getElementById("sede-municipio").value,
  }

  try {
    let response
    let mensaje

    if (modoEdicion.activo && modoEdicion.tipo === "sede") {
      // Modo actualización
      response = await fetch(`${API_BASE_URL}/sedes/${modoEdicion.id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: "Basic " + btoa("admin:admin123"),
        },
        body: JSON.stringify(formData),
      })
      mensaje = "Sede actualizada correctamente"
    } else {
      // Modo creación
      response = await fetch(`${API_BASE_URL}/sedes`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: "Basic " + btoa("admin:admin123"),
        },
        body: JSON.stringify(formData),
      })
      mensaje = "Sede registrada correctamente"
    }

    if (response.ok) {
      const sede = await response.json()

      if (modoEdicion.activo && modoEdicion.tipo === "sede") {
        // Actualizar la lista local
        const index = sedes.findIndex((s) => s.id === modoEdicion.id)
        if (index !== -1) {
          sedes[index] = sede
        }
        // Resetear modo edición
        modoEdicion = { activo: false, tipo: null, id: null }
        resetearFormulario("sede-form")
      } else {
        // Agregar nueva sede
        sedes.push(sede)
      }

      displaySedes()
      populateSedeSelect()
      document.getElementById("sede-form").reset()
      showModal("Éxito", mensaje, "success")
    } else {
      const error = await response.json()
      showModal("Error", error.message || "Error al procesar la sede", "error")
    }
  } catch (error) {
    console.error("Error:", error)
    showModal("Error", "Error de conexión", "error")
  }
}

async function handleAlmacenSubmit(event) {
  event.preventDefault()

  const formData = {
    sedeId: Number.parseInt(document.getElementById("almacen-sede").value),
    precioVenta: Number.parseFloat(document.getElementById("almacen-precio-venta").value),
    precioRenta: Number.parseFloat(document.getElementById("almacen-precio-renta").value),
    tamano: document.getElementById("almacen-tamano").value,
  }

  try {
    let response
    let mensaje

    if (modoEdicion.activo && modoEdicion.tipo === "almacen") {
      // Modo actualización
      response = await fetch(`${API_BASE_URL}/almacenes/${modoEdicion.id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: "Basic " + btoa("admin:admin123"),
        },
        body: JSON.stringify(formData),
      })
      mensaje = "Almacén actualizado correctamente"
    } else {
      // Modo creación
      response = await fetch(`${API_BASE_URL}/almacenes`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: "Basic " + btoa("admin:admin123"),
        },
        body: JSON.stringify(formData),
      })
      mensaje = "Almacén registrado correctamente"
    }

    if (response.ok) {
      const almacen = await response.json()

      if (modoEdicion.activo && modoEdicion.tipo === "almacen") {
        // Actualizar las listas locales
        const index = almacenes.findIndex((a) => a.id === modoEdicion.id)
        if (index !== -1) {
          almacenes[index] = almacen
        }

        const indexDisponibles = almacenesDisponibles.findIndex((a) => a.id === modoEdicion.id)
        if (indexDisponibles !== -1 && almacen.disponible) {
          almacenesDisponibles[indexDisponibles] = almacen
        }

        // Resetear modo edición
        modoEdicion = { activo: false, tipo: null, id: null }
        resetearFormulario("almacen-form")
      } else {
        // Agregar nuevo almacén
        almacenes.push(almacen)
        almacenesDisponibles.push(almacen)
      }

      displayAlmacenes(almacenes)
      updateDashboard()
      document.getElementById("almacen-form").reset()
      showModal("Éxito", mensaje, "success")
    } else {
      const error = await response.json()
      showModal("Error", error.message || "Error al procesar el almacén", "error")
    }
  } catch (error) {
    console.error("Error:", error)
    showModal("Error", "Error de conexión", "error")
  }
}

async function handleClienteSubmit(event) {
  event.preventDefault()

  const formData = {
    nombreCompleto: document.getElementById("cliente-nombre").value,
    telefono: document.getElementById("cliente-telefono").value,
    correoElectronico: document.getElementById("cliente-email").value,
  }

  try {
    const response = await fetch(`${API_BASE_URL}/clientes`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    })

    if (response.ok) {
      const nuevoCliente = await response.json()
      clientes.push(nuevoCliente)
      displayClientes()
      populateClienteSelect()
      document.getElementById("cliente-form").reset()
      showModal("Éxito", "Cliente registrado correctamente", "success")
    } else {
      const error = await response.json()
      showModal("Error", error.message || "Error al registrar el cliente", "error")
    }
  } catch (error) {
    console.error("Error:", error)
    showModal("Error", "Error de conexión", "error")
  }
}

async function handleTransaccionSubmit(event) {
  event.preventDefault()

  const formData = {
    clienteId: Number.parseInt(document.getElementById("transaccion-cliente").value),
    almacenId: Number.parseInt(document.getElementById("transaccion-almacen").value),
    tipoTransaccion: document.getElementById("transaccion-tipo").value,
  }

  try {
    const response = await fetch(`${API_BASE_URL}/almacenes/transaccion`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    })

    if (response.ok) {
      const transaccion = await response.json()

      // Actualizar listas locales
      const almacenIndex = almacenesDisponibles.findIndex((a) => a.id === transaccion.id)
      if (almacenIndex !== -1) {
        almacenesDisponibles.splice(almacenIndex, 1)
      }

      // Recargar datos
      await loadAllData()
      updateDashboard()
      populateAlmacenSelect()
      loadTransacciones()

      document.getElementById("transaccion-form").reset()
      showModal("Éxito", `Transacción de ${formData.tipoTransaccion.toLowerCase()} procesada correctamente`, "success")
    } else {
      const error = await response.json()
      showModal("Error", error.message || "Error al procesar la transacción", "error")
    }
  } catch (error) {
    console.error("Error:", error)
    showModal("Error", "Error de conexión", "error")
  }
}

// Funciones de visualización
function updateDashboard() {
  document.getElementById("total-sedes").textContent = sedes.length
  document.getElementById("total-almacenes").textContent = almacenes.length
  document.getElementById("almacenes-disponibles").textContent = almacenesDisponibles.length
  document.getElementById("total-clientes").textContent = clientes.length

  displayAlmacenesDisponibles()
}

// Actualizar la función displaySedes para incluir botones de acción
function displaySedes() {
  const container = document.getElementById("sedes-list")
  container.innerHTML = ""

  sedes.forEach((sede) => {
    const sedeCard = document.createElement("div")
    sedeCard.className = "sede-card"
    sedeCard.innerHTML = `
            <h4>${sede.clave}</h4>
            <p><strong>Estado:</strong> ${sede.estado}</p>
            <p><strong>Municipio:</strong> ${sede.municipio}</p>
            <div class="card-actions">
                <button class="btn-edit" onclick="editarSede(${sede.id})">
                    Editar
                </button>
                <button class="btn-delete" onclick="eliminarSede(${sede.id})">
                    Eliminar
                </button>
            </div>
        `
    container.appendChild(sedeCard)
  })
}

// Actualizar la función displayClientes para incluir botones de acción
function displayClientes() {
  const container = document.getElementById("clientes-list")
  container.innerHTML = ""

  clientes.forEach((cliente) => {
    const clienteCard = document.createElement("div")
    clienteCard.className = "cliente-card"
    clienteCard.innerHTML = `
            <h4>${cliente.nombreCompleto}</h4>
            <p><strong>Teléfono:</strong> ${cliente.telefono}</p>
            <p><strong>Email:</strong> ${cliente.correoElectronico}</p>
            <div class="card-actions">
                <button class="btn-edit" onclick="editarCliente(${cliente.id})">
                    Editar
                </button>
                <button class="btn-delete" onclick="eliminarCliente(${cliente.id})">
                    Eliminar
                </button>
            </div>
        `
    container.appendChild(clienteCard)
  })
}

function displayAlmacenes(almacenesList) {
  const container = document.getElementById("almacenes-list")
  container.innerHTML = ""

  almacenesList.forEach((almacen) => {
    const almacenCard = createAlmacenCard(almacen)
    container.appendChild(almacenCard)
  })
}

function displayAlmacenesDisponibles() {
  const container = document.getElementById("almacenes-disponibles-list")
  container.innerHTML = ""

  almacenesDisponibles.slice(0, 6).forEach((almacen) => {
    const almacenCard = createAlmacenCard(almacen, true)
    container.appendChild(almacenCard)
  })
}

function displayTransacciones(almacenesOcupados) {
  const container = document.getElementById("transacciones-list")
  container.innerHTML = ""

  almacenesOcupados.forEach((almacen) => {
    const almacenCard = createAlmacenCard(almacen, false, true)
    container.appendChild(almacenCard)
  })
}

// Agregar función para eliminar almacenes en las cards
function createAlmacenCard(almacen, showActions = false, showLiberar = false) {
  const card = document.createElement("div")
  card.className = `almacen-card ${almacen.disponible ? "disponible" : "ocupado"}`

  const tamanoTexto = {
    P: "Pequeño",
    M: "Mediano",
    G: "Grande",
  }

  card.innerHTML = `
        <div class="almacen-status ${almacen.disponible ? "disponible" : "ocupado"}">
            ${almacen.disponible ? "Disponible" : "Ocupado"}
        </div>
        <div class="almacen-info">
            <h4>${almacen.clave}</h4>
            <p><strong>Sede:</strong> ${almacen.sedeEstado}, ${almacen.sedeMunicipio}</p>
            <p><strong>Tamaño:</strong> ${tamanoTexto[almacen.tamano]}</p>
            <p><strong>Precio Venta:</strong> $${almacen.precioVenta.toLocaleString()}</p>
            <p><strong>Precio Renta:</strong> $${almacen.precioRenta.toLocaleString()}</p>
            ${almacen.clienteNombre ? `<p><strong>Cliente:</strong> ${almacen.clienteNombre}</p>` : ""}
        </div>
        <div class="almacen-actions">
            ${
              showActions && almacen.disponible
                ? `
                <button class="btn-comprar" onclick="iniciarTransaccion(${almacen.id}, 'COMPRA')">
                    Comprar
                </button>
                <button class="btn-rentar" onclick="iniciarTransaccion(${almacen.id}, 'RENTA')">
                    Rentar
                </button>
            `
                : ""
            }
            ${
              showLiberar && !almacen.disponible
                ? `
                <button class="btn-liberar" onclick="liberarAlmacen(${almacen.id})">
                    Liberar
                </button>
            `
                : ""
            }
            <button class="btn-edit" onclick="editarAlmacen(${almacen.id})" title="Editar almacén">
                Editar
            </button>
            <button class="btn-delete" onclick="eliminarAlmacen(${almacen.id})" title="Eliminar almacén">
                Eliminar
            </button>
        </div>
    `

  return card
}

// Funciones de filtrado
function filterAlmacenes(tipo) {
  // Actualizar botones activos
  document.querySelectorAll(".filter-btn").forEach((btn) => {
    btn.classList.remove("active")
  })
  event.target.classList.add("active")

  let almacenesFiltrados
  switch (tipo) {
    case "disponibles":
      almacenesFiltrados = almacenes.filter((a) => a.disponible)
      break
    case "ocupados":
      almacenesFiltrados = almacenes.filter((a) => !a.disponible)
      break
    default:
      almacenesFiltrados = almacenes
  }

  displayAlmacenes(almacenesFiltrados)
}

// Funciones de transacciones
function iniciarTransaccion(almacenId, tipo) {
  // Cambiar a la sección de transacciones
  showSection("transacciones")

  // Pre-llenar el formulario
  document.getElementById("transaccion-almacen").value = almacenId
  document.getElementById("transaccion-tipo").value = tipo
}

async function liberarAlmacen(almacenId) {
  if (!confirm("¿Está seguro de que desea liberar este almacén?")) {
    return
  }

  try {
    const response = await fetch(`${API_BASE_URL}/almacenes/${almacenId}/liberar`, {
      method: "PUT",
      headers: {
        Authorization: "Basic " + btoa("admin:admin123"),
      },
    })

    if (response.ok) {
      await loadAllData()
      updateDashboard()
      loadTransacciones()
      showModal("Éxito", "Almacén liberado correctamente", "success")
    } else {
      const error = await response.json()
      showModal("Error", error.message || "Error al liberar el almacén", "error")
    }
  } catch (error) {
    console.error("Error:", error)
    showModal("Error", "Error de conexión", "error")
  }
}

// Funciones de población de selects
function populateSelects() {
  populateSedeSelect()
  populateClienteSelect()
  populateAlmacenSelect()
}

function populateSedeSelect() {
  const select = document.getElementById("almacen-sede")
  select.innerHTML = '<option value="">Seleccionar sede...</option>'

  sedes.forEach((sede) => {
    const option = document.createElement("option")
    option.value = sede.id
    option.textContent = `${sede.clave} - ${sede.estado}, ${sede.municipio}`
    select.appendChild(option)
  })
}

function populateClienteSelect() {
  const select = document.getElementById("transaccion-cliente")
  select.innerHTML = '<option value="">Seleccionar cliente...</option>'

  clientes.forEach((cliente) => {
    const option = document.createElement("option")
    option.value = cliente.id
    option.textContent = cliente.nombreCompleto
    select.appendChild(option)
  })
}

function populateAlmacenSelect() {
  const select = document.getElementById("transaccion-almacen")
  select.innerHTML = '<option value="">Seleccionar almacén...</option>'

  almacenesDisponibles.forEach((almacen) => {
    const option = document.createElement("option")
    option.value = almacen.id
    option.textContent = `${almacen.clave} - ${almacen.sedeEstado}, ${almacen.sedeMunicipio}`
    select.appendChild(option)
  })
}

// Funciones de modal
function showModal(title, message, type = "info") {
  const modal = document.getElementById("modal")
  const modalBody = document.getElementById("modal-body")

  const typeClass = type === "success" ? "success-message" : type === "error" ? "error-message" : ""

  modalBody.innerHTML = `
        <h3>${title}</h3>
        <div class="${typeClass}">
            <p>${message}</p>
        </div>
    `

  modal.style.display = "block"
}

function closeModal() {
  document.getElementById("modal").style.display = "none"
}

// Función de utilidad para formatear números
function formatCurrency(amount) {
  return new Intl.NumberFormat("es-MX", {
    style: "currency",
    currency: "MXN",
  }).format(amount)
}

// Función de utilidad para formatear fechas
function formatDate(dateString) {
  const date = new Date(dateString)
  return date.toLocaleDateString("es-MX")
}

// Agregar funciones para editar y eliminar sedes
async function editarSede(sedeId) {
  const sede = sedes.find((s) => s.id === sedeId)
  if (!sede) return

  // Activar modo edición
  modoEdicion = { activo: true, tipo: "sede", id: sedeId }

  // Pre-llenar el formulario con los datos actuales
  document.getElementById("sede-estado").value = sede.estado
  document.getElementById("sede-municipio").value = sede.municipio

  // Cambiar la apariencia del formulario
  const form = document.getElementById("sede-form")
  const submitButton = form.querySelector('button[type="submit"]')

  // Cambiar texto del botón
  submitButton.textContent = "Actualizar Sede"
  submitButton.style.background = "linear-gradient(135deg, #ffc107 0%, #fd7e14 100%)"

  // Agregar botón de cancelar
  let cancelButton = form.querySelector(".btn-cancel")
  if (!cancelButton) {
    cancelButton = document.createElement("button")
    cancelButton.type = "button"
    cancelButton.className = "btn-cancel"
    cancelButton.textContent = "Cancelar"
    cancelButton.style.background = "#6c757d"
    cancelButton.style.marginLeft = "10px"
    cancelButton.onclick = () => cancelarEdicionSede()
    submitButton.parentNode.appendChild(cancelButton)
  }

  // Scroll al formulario
  form.scrollIntoView({ behavior: "smooth" })
}

function cancelarEdicionSede() {
  modoEdicion = { activo: false, tipo: null, id: null }
  resetearFormulario("sede-form")
}

function resetearFormulario(formId) {
  const form = document.getElementById(formId)
  const submitButton = form.querySelector('button[type="submit"]')
  const cancelButton = form.querySelector(".btn-cancel")

  // Restaurar el formulario
  form.reset()

  // Restaurar el botón de submit
  if (formId === "sede-form") {
    submitButton.textContent = "Registrar Sede"
  }
  submitButton.style.background = "linear-gradient(135deg, #667eea 0%, #764ba2 100%)"

  // Remover botón de cancelar
  if (cancelButton) {
    cancelButton.remove()
  }
}

async function eliminarSede(sedeId) {
  const sede = sedes.find((s) => s.id === sedeId)
  if (!sede) return

  if (!confirm(`¿Está seguro de que desea eliminar la sede "${sede.clave}"?`)) {
    return
  }

  try {
    const response = await fetch(`${API_BASE_URL}/sedes/${sedeId}`, {
      method: "DELETE",
      headers: {
        Authorization: "Basic " + btoa("admin:admin123"),
      },
    })

    if (response.ok) {
      // Remover de la lista local
      sedes = sedes.filter((s) => s.id !== sedeId)
      displaySedes()
      populateSedeSelect()
      updateDashboard()
      showModal("Éxito", "Sede eliminada correctamente", "success")
    } else {
      const error = await response.json()
      showModal("Error", error.message || "Error al eliminar la sede", "error")
    }
  } catch (error) {
    console.error("Error:", error)
    showModal("Error", "Error de conexión", "error")
  }
}

// Agregar funciones para editar y eliminar clientes
async function editarCliente(clienteId) {
  const cliente = clientes.find((c) => c.id === clienteId)
  if (!cliente) return

  // Pre-llenar el formulario con los datos actuales
  document.getElementById("cliente-nombre").value = cliente.nombreCompleto
  document.getElementById("cliente-telefono").value = cliente.telefono
  document.getElementById("cliente-email").value = cliente.correoElectronico

  // Cambiar el comportamiento del formulario para actualización
  const form = document.getElementById("cliente-form")
  const submitButton = form.querySelector('button[type="submit"]')

  // Guardar el handler original
  const originalHandler = form.onsubmit

  // Cambiar texto del botón
  submitButton.textContent = "Actualizar Cliente"
  submitButton.style.background = "linear-gradient(135deg, #ffc107 0%, #fd7e14 100%)"

  // Agregar botón de cancelar
  let cancelButton = form.querySelector(".btn-cancel")
  if (!cancelButton) {
    cancelButton = document.createElement("button")
    cancelButton.type = "button"
    cancelButton.className = "btn-cancel"
    cancelButton.textContent = "Cancelar"
    cancelButton.style.background = "#6c757d"
    cancelButton.style.marginLeft = "10px"
    cancelButton.onclick = () => cancelarEdicion("cliente-form", originalHandler)
    submitButton.parentNode.appendChild(cancelButton)
  }

  // Cambiar el handler del formulario
  form.onsubmit = async (event) => {
    event.preventDefault()
    await actualizarCliente(clienteId, originalHandler)
  }

  // Scroll al formulario
  form.scrollIntoView({ behavior: "smooth" })
}

async function actualizarCliente(clienteId, originalHandler) {
  const formData = {
    nombreCompleto: document.getElementById("cliente-nombre").value,
    telefono: document.getElementById("cliente-telefono").value,
    correoElectronico: document.getElementById("cliente-email").value,
  }

  try {
    const response = await fetch(`${API_BASE_URL}/clientes/${clienteId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    })

    if (response.ok) {
      const clienteActualizado = await response.json()

      // Actualizar la lista local
      const index = clientes.findIndex((c) => c.id === clienteId)
      if (index !== -1) {
        clientes[index] = clienteActualizado
      }

      displayClientes()
      populateClienteSelect()
      cancelarEdicion("cliente-form", originalHandler)
      showModal("Éxito", "Cliente actualizado correctamente", "success")
    } else {
      const error = await response.json()
      showModal("Error", error.message || "Error al actualizar el cliente", "error")
    }
  } catch (error) {
    console.error("Error:", error)
    showModal("Error", "Error de conexión", "error")
  }
}

async function eliminarCliente(clienteId) {
  const cliente = clientes.find((c) => c.id === clienteId)
  if (!cliente) return

  if (!confirm(`¿Está seguro de que desea eliminar el cliente "${cliente.nombreCompleto}"?`)) {
    return
  }

  try {
    const response = await fetch(`${API_BASE_URL}/clientes/${clienteId}`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Basic " + btoa("admin:admin123"),
      },
    })

    if (response.ok) {
      // Remover de la lista local
      clientes = clientes.filter((c) => c.id !== clienteId)
      displayClientes()
      populateClienteSelect()
      updateDashboard()
      showModal("Éxito", "Cliente eliminado correctamente", "success")
    } else {
      const error = await response.json()
      showModal("Error", error.message || "Error al eliminar el cliente", "error")
    }
  } catch (error) {
    console.error("Error:", error)
    showModal("Error", "Error de conexión", "error")
  }
}

// Función para cancelar edición
function cancelarEdicion(formId, originalHandler) {
  const form = document.getElementById(formId)
  const submitButton = form.querySelector('button[type="submit"]')
  const cancelButton = form.querySelector(".btn-cancel")

  // Restaurar el formulario
  form.reset()
  form.onsubmit = originalHandler

  // Restaurar el botón de submit
  if (formId === "sede-form") {
    submitButton.textContent = "Registrar Sede"
  } else if (formId === "cliente-form") {
    submitButton.textContent = "Registrar Cliente"
  } else if (formId === "almacen-form") {
    submitButton.textContent = "Registrar Almacén"
  }
  submitButton.style.background = "linear-gradient(135deg, #667eea 0%, #764ba2 100%)"

  // Remover botón de cancelar
  if (cancelButton) {
    cancelButton.remove()
  }
}

// Agregar función para eliminar almacenes
async function eliminarAlmacen(almacenId) {
  const almacen = almacenes.find((a) => a.id === almacenId)
  if (!almacen) {
    showModal("Error", "Almacén no encontrado", "error")
    return
  }

  if (!almacen.disponible) {
    showModal("Error", "No se puede eliminar un almacén ocupado. Primero debe liberarlo.", "error")
    return
  }

  if (!confirm(`¿Está seguro de que desea eliminar el almacén "${almacen.clave}"?`)) {
    return
  }

  try {
    const response = await fetch(`${API_BASE_URL}/almacenes/${almacenId}`, {
      method: "DELETE",
      headers: {
        Authorization: "Basic " + btoa("admin:admin123"),
      },
    })

    if (response.ok) {
      // Remover de las listas locales
      almacenes = almacenes.filter((a) => a.id !== almacenId)
      almacenesDisponibles = almacenesDisponibles.filter((a) => a.id !== almacenId)

      displayAlmacenes(almacenes)
      updateDashboard()
      showModal("Éxito", "Almacén eliminado correctamente", "success")
    } else {
      const errorData = await response.json().catch(() => ({ message: "Error desconocido" }))
      showModal("Error", errorData.message || "Error al eliminar el almacén", "error")
    }
  } catch (error) {
    console.error("Error:", error)
    showModal("Error", "Error de conexión al servidor", "error")
  }
}

// Función para editar almacenes
async function editarAlmacen(almacenId) {
  const almacen = almacenes.find((a) => a.id === almacenId)
  if (!almacen) return

  // Activar modo edición
  modoEdicion = { activo: true, tipo: "almacen", id: almacenId }

  // Pre-llenar el formulario con los datos actuales
  document.getElementById("almacen-sede").value = almacen.sedeId
  document.getElementById("almacen-precio-venta").value = almacen.precioVenta
  document.getElementById("almacen-precio-renta").value = almacen.precioRenta
  document.getElementById("almacen-tamano").value = almacen.tamano

  // Cambiar la apariencia del formulario
  const form = document.getElementById("almacen-form")
  const submitButton = form.querySelector('button[type="submit"]')

  // Cambiar texto del botón
  submitButton.textContent = "Actualizar Almacén"
  submitButton.style.background = "linear-gradient(135deg, #ffc107 0%, #fd7e14 100%)"

  // Agregar botón de cancelar
  let cancelButton = form.querySelector(".btn-cancel")
  if (!cancelButton) {
    cancelButton = document.createElement("button")
    cancelButton.type = "button"
    cancelButton.className = "btn-cancel"
    cancelButton.textContent = "Cancelar"
    cancelButton.style.background = "#6c757d"
    cancelButton.style.marginLeft = "10px"
    cancelButton.onclick = () => cancelarEdicionAlmacen()
    submitButton.parentNode.appendChild(cancelButton)
  }

  // Cambiar a la sección de almacenes y scroll al formulario
  showSection("almacenes")
  setTimeout(() => {
    form.scrollIntoView({ behavior: "smooth" })
  }, 100)
}

function cancelarEdicionAlmacen() {
  modoEdicion = { activo: false, tipo: null, id: null }
  resetearFormulario("almacen-form")
}
