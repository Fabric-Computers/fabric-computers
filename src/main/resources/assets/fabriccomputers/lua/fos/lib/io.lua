local event = os.loadLibrary("event")
local keys = os.loadLibrary("keys")

local io = {}

io.screen = nil

for i=0, 5 do
    local component = computer:getComponent(i)
    if component then
        if component:getComponentType() == "screen" then
            io.screen = component
        end
    end
end

function io.setPixel(x, y, color)
    if io.screen then
        io.screen:setPixel(x, y, color)
    end
end

_G.charMap = {}

function io.setFont(font)
    _G.charMap = font
end

function io.getGlyphSize(g)
    local maxHeight = 0
    local width = 0

    if _G.charMap[g] then
        if _G.charMap[g]["h"] > maxHeight then
            maxHeight = _G.charMap[g]["h"]
        end

        width = width + _G.charMap[g]["w"] + 1
    end

    return width, maxHeight
end

function io.putChar(character, xPos, yPos, foreground, background)
    if _G.charMap[character] and io.screen then
        local glyph = _G.charMap[character]

        for y=0, glyph["h"]-1 do
            for x=0, glyph["w"]-1 do
                if computer:crazyBitHack(glyph["b"][y+1], x) then
                    io.screen:setPixel(xPos + x, yPos + y, foreground)
                else
                    io.screen:setPixel(xPos + x, yPos + y, background)
                end
            end
        end
    end
end

if not _G.currentLine then
    _G.currentLine = 0
    _G.currentColumn = 0
end
io.currentForeground = 0xFFFFFF
io.currentBackground = 0x000000

function io.writeString(s, x, y, foreground, background)
    local cur = 0
    local prevWidth = 0
    for i=0, #s do
        if s:sub(i, i) then
            if _G.charMap[s:sub(i, i)] then
                io.putChar(s:sub(i, i), x + cur, y, foreground, background)
                prevWidth = _G.charMap[s:sub(i, i)]["w"]
                cur = cur + prevWidth + 1
            end
        end
    end
end

function io.dump(val)
    function dstr(o)
        if type(o) == 'table' then
            local s = '{ '
            for k,v in pairs(o) do
                if type(k) ~= 'number' then k = '"'..k..'"' end
                s = s .. '['..k..'] = ' .. dstr(v) .. ','
            end
            return s .. '} '
        else
            return tostring(o)
        end
    end

    io.print(dstr(val))
end

function io.print(s)
    if not s then
        s = "NIL"
    end

    print(s)

    --s = s:upper()

    local maxHeight = 0
    for i=0, #s do
        if _G.charMap[s:sub(i, i)] then
            if _G.charMap[s:sub(i, i)]["h"] > maxHeight then
                maxHeight = _G.charMap[s:sub(i, i)]["h"]
            end
        end
    end

    io.writeString(s, _G.currentColumn, _G.currentLine, io.currentForeground, io.currentBackground)

    _G.currentLine = _G.currentLine + maxHeight + 1
end

function io.writeFile(path, content)
    fs:writeFile(path, content)
end

function io.readFile(path)
    return fs:readFile(path)
end

function io.makeDir(path)
    return fs:makeDir(path)
end

function io.remove(path)
    return fs:remove(path)
end

function io.write(s)
    local maxHeight = 0
    local length = 0
    local prevWidth = 0
    for i=0, #s do
        if _G.charMap[s:sub(i, i)] then
            prevWidth = _G.charMap[s:sub(i, i)]["w"]
            length = length + prevWidth + 1
            if _G.charMap[s:sub(i, i)]["h"] > maxHeight then
                maxHeight = _G.charMap[s:sub(i, i)]["h"]
            end
        end
    end

    io.writeString(s, _G.currentColumn, _G.currentLine, io.currentForeground, io.currentBackground)

    _G.currentColumn = _G.currentColumn + length
end

function io.clear(color)
    if screen then
        local size = screen:getScreenSize()
        for x=0, size[1] do
            for y=0, size[2] do
                io.setPixel(x, y, color)
            end
        end
    end
end

function io.getStringSize(s)
    s = s:upper()

    local maxHeight = 0
    local width = 0

    for i=0, #s do
        local c = s:sub(i, i)
        if _G.charMap[c] then
            if _G.charMap[c]["h"] > maxHeight then
                maxHeight = _G.charMap[c]["h"]
            end

            width = width + _G.charMap[c]["w"] + 1
        end
    end

    return width, maxHeight
end

function io.readLine()
    local x = _G.currentColumn
    local y = _G.currentLine
    local result = ""
    while true do
        eventName, key = event.pollEvents()
        if eventName == "key_down" then
            if key == keys.BACKSPACE then
                local width, height = io.getStringSize(result)
                for xPos=0, width do
                    for yPos=0, height do
                        io.setPixel(x+xPos, y+yPos, io.backgroundColor)
                    end
                end

                result = result:sub(1, -2)
                io.setCursor(x, y)
                io.print(result)
            elseif key == keys.ENTER then
                return result
            end
        elseif eventName == "char" then
            result = result .. string.char(key)
            io.setCursor(x, y)
            io.print(result)
        end
    end
end

function io.exists(path)
    return _G.fileSystem:exists(path)
end

function io.error(message)
    local foregroundColor = io.currentForeground
    local backgroundColor = io.backgroundColor
    io.setBackground(0x000000)
    io.setForeground(0xFFFFFF)
    io.print(message)
    io.setForeground(foregroundColor)
    io.setBackground(backgroundColor)
end

function io.setForeground(color)
    io.currentForeground = color
end

function io.setBackground(color)
    io.currentBackground = color
end

function io.setCursor(x, y)
    _G.currentLine = y
    _G.currentColumn = x
end

return io