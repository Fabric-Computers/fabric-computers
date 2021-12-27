io = {}

function io.setPixel(x, y, color)
    computer:setPixel(x, y, color)
end

local charMap = {}

charMap["A"] = {}

charMap["A"]["width"] = 4
charMap["A"]["height"] = 5
charMap["A"]["2 1"] = true
charMap["A"]["3 1"] = true
charMap["A"]["1 2"] = true
charMap["A"]["1 3"] = true
charMap["A"]["1 4"] = true
charMap["A"]["1 5"] = true
charMap["A"]["2 3"] = true
charMap["A"]["3 3"] = true
charMap["A"]["4 2"] = true
charMap["A"]["4 3"] = true
charMap["A"]["4 4"] = true
charMap["A"]["4 5"] = true

charMap["B"] = {}

charMap["B"]["width"] = 4
charMap["B"]["height"] = 5
charMap["B"]["1 1"] = true
charMap["B"]["1 2"] = true
charMap["B"]["1 3"] = true
charMap["B"]["1 4"] = true
charMap["B"]["1 5"] = true
charMap["B"]["2 1"] = true
charMap["B"]["3 1"] = true
charMap["B"]["4 2"] = true
charMap["B"]["3 3"] = true
charMap["B"]["2 3"] = true
charMap["B"]["4 4"] = true
charMap["B"]["3 5"] = true
charMap["B"]["2 5"] = true

charMap["C"] = {}

charMap["C"]["width"] = 4
charMap["C"]["height"] = 5
charMap["C"]["2 1"] = true
charMap["C"]["3 1"] = true
charMap["C"]["4 1"] = true
charMap["C"]["1 2"] = true
charMap["C"]["1 3"] = true
charMap["C"]["1 4"] = true
charMap["C"]["2 5"] = true
charMap["C"]["3 5"] = true
charMap["C"]["4 5"] = true

charMap["D"] = {}

charMap["D"]["width"] = 4
charMap["D"]["height"] = 5
charMap["D"]["1 1"] = true
charMap["D"]["1 2"] = true
charMap["D"]["1 3"] = true
charMap["D"]["1 4"] = true
charMap["D"]["1 5"] = true
charMap["D"]["2 1"] = true
charMap["D"]["3 1"] = true
charMap["D"]["4 2"] = true
charMap["D"]["4 3"] = true
charMap["D"]["4 4"] = true
charMap["D"]["2 5"] = true
charMap["D"]["3 5"] = true

charMap["E"] = {}

charMap["E"]["width"] = 4
charMap["E"]["height"] = 5
charMap["E"]["1 1"] = true
charMap["E"]["1 2"] = true
charMap["E"]["1 3"] = true
charMap["E"]["1 4"] = true
charMap["E"]["1 5"] = true
charMap["E"]["2 1"] = true
charMap["E"]["3 1"] = true
charMap["E"]["4 1"] = true
charMap["E"]["2 3"] = true
charMap["E"]["3 3"] = true
charMap["E"]["2 5"] = true
charMap["E"]["3 5"] = true
charMap["E"]["4 5"] = true

charMap["F"] = {}

charMap["F"]["width"] = 4
charMap["F"]["height"] = 5
charMap["F"]["1 1"] = true
charMap["F"]["1 2"] = true
charMap["F"]["1 3"] = true
charMap["F"]["1 4"] = true
charMap["F"]["1 5"] = true
charMap["F"]["2 1"] = true
charMap["F"]["3 1"] = true
charMap["F"]["4 1"] = true
charMap["F"]["2 3"] = true
charMap["F"]["3 3"] = true

charMap["G"] = {}

charMap["G"]["width"] = 4
charMap["G"]["height"] = 5
charMap["G"]["1 2"] = true
charMap["G"]["1 3"] = true
charMap["G"]["1 4"] = true
charMap["G"]["2 1"] = true
charMap["G"]["3 1"] = true
charMap["G"]["4 1"] = true
charMap["G"]["2 5"] = true
charMap["G"]["3 5"] = true
charMap["G"]["4 5"] = true
charMap["G"]["4 4"] = true
charMap["G"]["4 3"] = true
charMap["G"]["3 3"] = true

charMap["H"] = {}

charMap["H"]["width"] = 4
charMap["H"]["height"] = 5
charMap["H"]["1 1"] = true
charMap["H"]["1 2"] = true
charMap["H"]["1 3"] = true
charMap["H"]["1 4"] = true
charMap["H"]["1 5"] = true
charMap["H"]["2 3"] = true
charMap["H"]["3 3"] = true
charMap["H"]["4 1"] = true
charMap["H"]["4 2"] = true
charMap["H"]["4 3"] = true
charMap["H"]["4 4"] = true
charMap["H"]["4 5"] = true

charMap["I"] = {}

charMap["I"]["width"] = 3
charMap["I"]["height"] = 5
charMap["I"]["2 1"] = true
charMap["I"]["2 2"] = true
charMap["I"]["2 3"] = true
charMap["I"]["2 4"] = true
charMap["I"]["2 5"] = true

charMap["J"] = {}

charMap["J"]["width"] = 4
charMap["J"]["height"] = 5
charMap["J"]["4 1"] = true
charMap["J"]["4 2"] = true
charMap["J"]["4 3"] = true
charMap["J"]["4 4"] = true
charMap["J"]["1 4"] = true
charMap["J"]["2 5"] = true
charMap["J"]["3 5"] = true

charMap["K"] = {}

charMap["K"]["width"] = 4
charMap["K"]["height"] = 5
charMap["K"]["1 1"] = true
charMap["K"]["1 2"] = true
charMap["K"]["1 3"] = true
charMap["K"]["1 4"] = true
charMap["K"]["1 5"] = true
charMap["K"]["2 3"] = true
charMap["K"]["3 2"] = true
charMap["K"]["4 1"] = true
charMap["K"]["3 4"] = true
charMap["K"]["4 5"] = true

charMap["L"] = {}

charMap["L"]["width"] = 4
charMap["L"]["height"] = 5
charMap["L"]["1 1"] = true
charMap["L"]["1 2"] = true
charMap["L"]["1 3"] = true
charMap["L"]["1 4"] = true
charMap["L"]["1 5"] = true
charMap["L"]["2 5"] = true
charMap["L"]["3 5"] = true
charMap["L"]["4 5"] = true

charMap["M"] = {}

charMap["M"]["width"] = 5
charMap["M"]["height"] = 5
charMap["M"]["1 1"] = true
charMap["M"]["1 2"] = true
charMap["M"]["1 3"] = true
charMap["M"]["1 4"] = true
charMap["M"]["1 5"] = true
charMap["M"]["5 1"] = true
charMap["M"]["5 2"] = true
charMap["M"]["5 3"] = true
charMap["M"]["5 4"] = true
charMap["M"]["5 5"] = true
charMap["M"]["2 2"] = true
charMap["M"]["3 3"] = true
charMap["M"]["4 2"] = true

charMap["N"] = {}

charMap["N"]["width"] = 4
charMap["N"]["height"] = 5
charMap["N"]["1 1"] = true
charMap["N"]["1 2"] = true
charMap["N"]["1 3"] = true
charMap["N"]["1 4"] = true
charMap["N"]["1 5"] = true
charMap["N"]["4 1"] = true
charMap["N"]["4 2"] = true
charMap["N"]["4 3"] = true
charMap["N"]["4 4"] = true
charMap["N"]["4 5"] = true
charMap["N"]["2 2"] = true
charMap["N"]["3 3"] = true

charMap["O"] = {}

charMap["O"]["width"] = 4
charMap["O"]["height"] = 5
charMap["O"]["2 1"] = true
charMap["O"]["3 1"] = true
charMap["O"]["1 2"] = true
charMap["O"]["1 3"] = true
charMap["O"]["1 4"] = true
charMap["O"]["2 5"] = true
charMap["O"]["3 5"] = true
charMap["O"]["4 2"] = true
charMap["O"]["4 3"] = true
charMap["O"]["4 4"] = true

charMap["P"] = {}

charMap["P"]["width"] = 4
charMap["P"]["height"] = 5
charMap["P"]["1 1"] = true
charMap["P"]["1 2"] = true
charMap["P"]["1 3"] = true
charMap["P"]["1 4"] = true
charMap["P"]["1 5"] = true
charMap["P"]["2 1"] = true
charMap["P"]["3 1"] = true
charMap["P"]["4 2"] = true
charMap["P"]["3 3"] = true
charMap["P"]["2 3"] = true

charMap["Q"] = {}

charMap["Q"]["width"] = 4
charMap["Q"]["height"] = 5
charMap["Q"]["2 1"] = true
charMap["Q"]["3 1"] = true
charMap["Q"]["1 2"] = true
charMap["Q"]["1 3"] = true
charMap["Q"]["1 4"] = true
charMap["Q"]["2 5"] = true
charMap["Q"]["3 5"] = true
charMap["Q"]["4 2"] = true
charMap["Q"]["4 3"] = true
charMap["Q"]["4 4"] = true
charMap["Q"]["3 4"] = true
charMap["Q"]["4 5"] = true

charMap["R"] = {}

charMap["R"]["width"] = 4
charMap["R"]["height"] = 5
charMap["R"]["1 1"] = true
charMap["R"]["1 2"] = true
charMap["R"]["1 3"] = true
charMap["R"]["1 4"] = true
charMap["R"]["1 5"] = true
charMap["R"]["2 1"] = true
charMap["R"]["3 1"] = true
charMap["R"]["4 2"] = true
charMap["R"]["3 3"] = true
charMap["R"]["2 3"] = true
charMap["R"]["3 4"] = true
charMap["R"]["4 5"] = true

charMap["S"] = {}

charMap["S"]["width"] = 4
charMap["S"]["height"] = 5
charMap["S"]["2 1"] = true
charMap["S"]["3 1"] = true
charMap["S"]["4 1"] = true
charMap["S"]["1 2"] = true
charMap["S"]["2 3"] = true
charMap["S"]["3 3"] = true
charMap["S"]["4 4"] = true
charMap["S"]["1 5"] = true
charMap["S"]["2 5"] = true
charMap["S"]["3 5"] = true

charMap["T"] = {}

charMap["T"]["width"] = 3
charMap["T"]["height"] = 5
charMap["T"]["1 1"] = true
charMap["T"]["2 1"] = true
charMap["T"]["3 1"] = true
charMap["T"]["2 2"] = true
charMap["T"]["2 3"] = true
charMap["T"]["2 4"] = true
charMap["T"]["2 5"] = true

charMap["U"] = {}

charMap["U"]["width"] = 4
charMap["U"]["height"] = 5
charMap["U"]["1 1"] = true
charMap["U"]["1 2"] = true
charMap["U"]["1 3"] = true
charMap["U"]["1 4"] = true
charMap["U"]["1 5"] = true
charMap["U"]["2 5"] = true
charMap["U"]["3 5"] = true
charMap["U"]["4 1"] = true
charMap["U"]["4 2"] = true
charMap["U"]["4 3"] = true
charMap["U"]["4 4"] = true
charMap["U"]["4 5"] = true

charMap["V"] = {}

charMap["V"]["width"] = 3
charMap["V"]["height"] = 5
charMap["V"]["1 1"] = true
charMap["V"]["1 2"] = true
charMap["V"]["1 3"] = true
charMap["V"]["1 4"] = true
charMap["V"]["2 5"] = true
charMap["V"]["3 1"] = true
charMap["V"]["3 2"] = true
charMap["V"]["3 3"] = true
charMap["V"]["3 4"] = true

charMap["W"] = {}

charMap["W"]["width"] = 5
charMap["W"]["height"] = 5
charMap["W"]["1 1"] = true
charMap["W"]["1 2"] = true
charMap["W"]["1 3"] = true
charMap["W"]["1 4"] = true
charMap["W"]["2 5"] = true
charMap["W"]["5 1"] = true
charMap["W"]["5 2"] = true
charMap["W"]["5 3"] = true
charMap["W"]["5 4"] = true
charMap["W"]["3 4"] = true
charMap["W"]["4 5"] = true

charMap["X"] = {}

charMap["X"]["width"] = 3
charMap["X"]["height"] = 5
charMap["X"]["1 1"] = true
charMap["X"]["1 2"] = true
charMap["X"]["1 4"] = true
charMap["X"]["1 5"] = true
charMap["X"]["3 1"] = true
charMap["X"]["3 2"] = true
charMap["X"]["3 4"] = true
charMap["X"]["3 5"] = true
charMap["X"]["2 3"] = true

charMap["Y"] = {}

charMap["Y"]["width"] = 3
charMap["Y"]["height"] = 5
charMap["Y"]["1 1"] = true
charMap["Y"]["1 2"] = true
charMap["Y"]["1 3"] = true
charMap["Y"]["3 1"] = true
charMap["Y"]["3 2"] = true
charMap["Y"]["3 3"] = true
charMap["Y"]["2 3"] = true
charMap["Y"]["2 4"] = true
charMap["Y"]["2 5"] = true

charMap["Z"] = {}

charMap["Z"]["width"] = 4
charMap["Z"]["height"] = 5
charMap["Z"]["1 1"] = true
charMap["Z"]["2 1"] = true
charMap["Z"]["3 1"] = true
charMap["Z"]["4 1"] = true
charMap["Z"]["4 2"] = true
charMap["Z"]["3 3"] = true
charMap["Z"]["2 4"] = true
charMap["Z"]["1 5"] = true
charMap["Z"]["2 5"] = true
charMap["Z"]["3 5"] = true
charMap["Z"]["4 5"] = true

charMap["."] = {}

charMap["."]["width"] = 1
charMap["."]["height"] = 5
charMap["."]["1 5"] = true

charMap[","] = {}

charMap[","]["width"] = 1
charMap[","]["height"] = 5
charMap[","]["1 4"] = true
charMap[","]["1 5"] = true

charMap["!"] = {}

charMap["!"]["width"] = 1
charMap["!"]["height"] = 5
charMap["!"]["1 1"] = true
charMap["!"]["1 2"] = true
charMap["!"]["1 3"] = true
charMap["!"]["1 5"] = true

charMap["?"] = {}

charMap["?"]["width"] = 3
charMap["?"]["height"] = 5
charMap["?"]["1 1"] = true
charMap["?"]["2 1"] = true
charMap["?"]["3 2"] = true
charMap["?"]["1 3"] = true
charMap["?"]["2 3"] = true
charMap["?"]["1 5"] = true

charMap[" "] = {}
charMap[" "]["width"] = 4
charMap[" "]["height"] = 5

charMap["1"] = {}

charMap["1"]["width"] = 3
charMap["1"]["height"] = 5
charMap["1"]["2 1"] = true
charMap["1"]["2 2"] = true
charMap["1"]["2 3"] = true
charMap["1"]["2 4"] = true
charMap["1"]["2 5"] = true
charMap["1"]["1 5"] = true
charMap["1"]["3 5"] = true
charMap["1"]["1 2"] = true

function io.putChar(character, xPos, yPos, foreground, background)
    if charMap[character] then
        for x=0, charMap[character]["width"]-1 do
            for y=0, charMap[character]["height"]-1 do
                if charMap[character][tostring(x+1).." "..tostring(y+1)] then
                    if charMap[character][tostring(x+1).." "..tostring(y+1)] then
                        computer:setPixel(xPos + x, yPos + y, foreground)
                    end
                else
                    computer:setPixel(xPos + x, yPos + y, background)
                end
            end
        end
    end
end

io.currentLine = 0
io.currentColumn = 0
io.currentForeground = 0xFFFFFF
io.currentBackground = 0x000000

function io.writeString(s, x, y, foreground, background)
    local cur = 0
    local prevWidth = 0
    for i=0, #s do
        if s:sub(i, i) then
            if charMap[s:sub(i, i)] then
                io.putChar(s:sub(i, i), x + cur, y, foreground, background)
                prevWidth = charMap[s:sub(i, i)]["width"]
                cur = cur + prevWidth + 1
            end
        end
    end
end

function io.print(s)
    if not s then
        s = "NIL"
    end

    s = s:upper()

    local maxHeight = 0
    for i=0, #s do
        if charMap[s:sub(i, i)] then
            if charMap[s:sub(i, i)]["height"] > maxHeight then
                maxHeight = charMap[s:sub(i, i)]["height"]
            end
        end
    end

    io.writeString(s, io.currentColumn, io.currentLine, io.currentForeground, io.currentBackground)

    io.currentLine = io.currentLine + maxHeight + 1
end

function io.write(s)
    local maxHeight = 0
    local lenght = 0
    local prevWidth = 0
    for i=0, #s do
        if charMap[s:sub(i, i)] then
            prevWidth = charMap[s:sub(i, i)]["width"]
            lenght = lenght + prevWidth + 1
            if charMap[s:sub(i, i)]["height"] > maxHeight then
                maxHeight = charMap[s:sub(i, i)]["height"]
            end
        end
    end

    io.writeString(s, io.currentColumn, io.currentLine, io.currentForeground, io.currentBackground)

    io.currentColumn = io.currentColumn + lenght
    io.currentLine = io.currentLine + maxHeight + 1
end

function io.clear(color)
    local width, height = computer:getScreenSize()
    for x=0, width do
        for y=0, height do
            io.setPixel(x, y, color)
        end
    end
end

function io.setForeground(color)
    io.currentForeground = color
end

function io.setBackground(color)
    io.currentBackground = color
end

function io.setCursor(x, y)
    io.currentLine = y
    io.currentColumn = x
end