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

snake[5] = {}
snake[5]["x"] = 1
snake[5]["y"] = 5

snake[6] = {}
snake[6]["x"] = 1
snake[6]["y"] = 6

snake[7] = {}
snake[7]["x"] = 1
snake[7]["y"] = 7

snake[8] = {}
snake[8]["x"] = 1
snake[8]["y"] = 8

local direction = 0

local ARROW_DOWN = 264
local ARROW_UP = 265
local ARROW_LEFT = 263
local ARROW_RIGHT = 262

local dead = false

function isTouching(x, y)
    for i, k in pairs(snake) do
        if i ~= #snake then
            if k["x"] == x and k["y"] == y then
                dead = true
            end
        end
    end
end


function getScreenSize()
    if screen then
        local size = screen:getScreenSize()
        return size[1], size[2]
    end
    return 0, 0
end



for i=0, 5 do
    local component = computer:getComponent(i)
    if component then
        if component:getComponentType() == "screen" then
            screen = component
        end
    end
end

local width, height = getScreenSize()

local x, y = math.floor(width/2), math.floor(height/2)

local foodX, foodY = x+1, y+1

function draw()



    if not dead then

        isTouching(x, y)
        for xP=0, width-1 do
            for yP=0, height-1 do
                if screen then
                    screen:setPixel(xP, yP, 0x000000)
                end
            end
        end

        local rectX = math.floor((width/2) - 50)
        local rectY = math.floor((height/2) - 40)

        if x < rectX then x = rectX + 100 end
        if x > rectX + 100 then x = rectX end
        if y < rectY then y = rectY + 80 end
        if y > rectY + 80 then y = rectY end

        if x == foodX and y == foodY then
            foodX = math.random(rectX, rectX + 100)
            foodY = math.random(rectY, rectY + 80)
            local t = {}
            t["x"] = x
            t["y"] = y
            table.insert(snake, t)
        end

        for i=0, 100 do
            if screen then
                screen:setPixel(rectX + i, rectY, 0x0000FF)
                screen:setPixel(rectX + i, rectY + 80, 0x0000FF)
            end
        end

        for j=0, 80 do
            if screen then
                screen:setPixel(rectX, rectY + j, 0x0000FF)
                screen:setPixel(rectX + 100, rectY + j, 0x0000FF)
            end
        end

        screen:setPixel(foodX, foodY, 0xFFFF00)

        for i, k in pairs(snake) do
            screen:setPixel(k["x"], k["y"], 0xFF0000)
        end

        if direction == 0 then
            y = y + 1
        elseif direction == 1 then
            y = y - 1
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
    else
        for xP=0, width-1 do
            for yP=0, height-1 do
                if screen then
                    screen:setPixel(xP, yP, 0xFF0000)
                end
            end
        end
    end
end

function processInput(key)
    if key == ARROW_DOWN and direction ~= 1 then
        direction = 0
    elseif key == ARROW_UP and direction ~= 0 then
        direction = 1
    elseif key == ARROW_LEFT and direction ~= 3 then
        direction = 2
    elseif key == ARROW_RIGHT and direction ~= 2 then
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