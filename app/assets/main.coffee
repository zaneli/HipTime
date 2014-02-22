wsEndpoint = (path) ->
  loc = window.location
  protocol = if loc.protocol == "https:" then "wss:" else "ws:"
  "#{protocol}//#{loc.host}#{path}"

doConnect = (apiKey) ->
  connection = new WebSocket(wsEndpoint("/api/status"), ['soap'])

  sendJson = (obj) ->
    connection.send(JSON.stringify(obj))

  connection.onopen = ->
    sendJson(type: "api_key", value: apiKey)

  connection.onerror = (error) ->
    console.log(error)

  connection.onmessage = (message) ->
    data = JSON.parse(message.data)
    switch data.type
      when "users" then displayUsers(data.value)
      when "ping" then sendJson(type: "pong")
      else console.log("Unknown message", message)

gravatarUrl = (email) ->
  "http://www.gravatar.com/avatar/#{$.md5(email)}"

displayUsers = (users) ->

  genLiElement = (user) ->
    li = $("<li />").addClass("list-group-item")
    img = $("<img />").attr("src", gravatarUrl(user.email))
    li.append(img).append(user.name)

  genLiElements = (usersList) ->
    _.map usersList, (u) -> genLiElement(u)

  populateLi = (ul, usersList) ->
    ul.empty()
    _.each genLiElements(usersList), (li) -> ul.append(li)
  
  activeUsers = _.filter users, (user) -> user.active
  inactiveUsers = _.filter users, (user) -> !user.active
  populateLi($(".active-users-list"), activeUsers)
  populateLi($(".inactive-users-list"), inactiveUsers)



$ ->
  alertDiv = $(".alert")
  warningEl = $("#warning-message")
  apiKeyInput = $("input.api-key")
  startPane = $("#start-pane")
  userStatusPane = $("#user-status")

  $(".active-users-list").empty()
  $(".inactive-users-list").empty()
    
  $(".start-btn").click ->
    apiKey = $.trim(apiKeyInput.val())
    if apiKey.length > 0
      alertDiv.hide()
      startPane.fadeOut "normal", ->
        userStatusPane.show()
        doConnect(apiKey)
    else
      warningEl.text("Specify API key")
      alertDiv.show()

