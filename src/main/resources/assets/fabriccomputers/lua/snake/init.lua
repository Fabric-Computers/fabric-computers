local screen

print("Loading snake")

local snake = {}

snake[1] = {}
snake[1]["x"] = 1
snake[1]["y"] = 1

snake[2] = {}
snake[2]["x"] = 1
snake[2]["y"] = 2

snake[3] = {}
snake[3]["x"] = 1
snake[3]["y"] = 3

snake[4] = {}
snake[4]["x"] = 1
snake[4]["y"] = 4

local direction = 0

local ARROW_DOWN = 264
local ARROW_UP = 265
local ARROW_LEFT = 263
local ARROW_RIGHT = 262

local x, y = 0, 0

function getScreenSize()
    if screen then
        local size = screen:getScreenSize()
        return size[1], size[2]
    end
    return 0, 0
end

local foodX, foodY = 0, 0

for i=0, 5 do
    local component = computer:getComponent(i)
    if component then
        if component:getComponentType() == "screen" then
            screen = component
        end
    end
end

local width, height = getScreenSize()

function draw()

    if x == foodX and y == foodY then
        foodX = math.random(0, width)
        foodY = math.random(0, height)
        local t = {}
        t["x"] = x
        t["y"] = y
        table.insert(snake, t)
    end

    if x > foodX then direction = 2
    elseif y > foodY then direction = 1
    elseif x < foodX then direction = 3
    elseif y < foodY then direction = 0 end

    for xP=0, width-1 do
        for yP=0, height-1 do
            if screen then
                screen:setPixel(xP, yP, 0x000000)
            end
        end
    end

    screen:setPixel(foodX, foodY, 0xFFFF00)

    for i, k in pairs(snake) do
        screen:setPixel(k["x"], k["y"], 0xFF0000)
    end

    if direction == 0 then
        y = y + 1
    elseif direction == 1 then
        y = y -1
    elseif direction == 2 then
        x = x - 1
    elseif direction == 3 then
        x = x + 1
    end
    table.remove(snake, 1)
    local t = {}
    t["x"] = x
    t["y"] = y
    table.insert(snake, t)
end

function processInput(key)
    if key == ARROW_DOWN then
        direction = 0
    elseif key == ARROW_UP then
        direction = 1
    elseif key == ARROW_LEFT then
        direction = 2
    elseif key == ARROW_RIGHT then
        direction = 3
    end
end

function pollEvent()
    local ev = computer:pollEvent()
    while not ev do
        ev = computer:pollEvent()
    end

    return table.unpack(ev)
end

while true do
    eventName, key = pollEvent()
    if eventName == "tick" then
        draw()
    elseif eventName == "key_up" then
        processInput(key)
    end
end