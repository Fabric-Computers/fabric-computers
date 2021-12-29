-- Basic text editor
local io = os.loadLibrary("io")

local cursorX = 0
local cursorY = 0

if not io.screen then
    error("Screen is required!")
end

local screenWidth = io.screen:getScreenSize()[1]
local screenHeight = io.screen:getScreenSize()[2]

local buffer = {}

function initBuffer()
    local fontWidth, fontHeight = io.getGlyphSize(" ")

    for i=0, math.floor(screenWidth / fontWidth) do
        for j=0, math.floor(screenHeight / fontHeight) do
            if not buffer[j] then
                buffer[j] = {}
            end

            buffer[j][i] = ""
        end
    end
end

function clear()
    if io.screen then
        local size = io.screen:getScreenSize()
        for x=0, size[1] do
            for y=0, size[2] do
                io.setPixel(x, y, color)
            end
        end
    end
    io.currentLine = 0
    io.currentColumn = 0
    cursorX = 0
    cursorY = 0
end

function draw()
    while true do
        clear()

        for i, k in pairs(buffer) do
            for j, v in pairs(buffer[i]) do
                io.write(v)
            end
            io.currentLine = io.currentLine + 1
        end

        computer:sleep(500000000)
    end
end

draw()