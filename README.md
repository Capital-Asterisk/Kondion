![Logo](http://vendalenger.x10host.com/img/Kondion_github.png)

####WARNING: Discontinued!

==================================
The Kondion Game engine is a discontinued modular game engine made in Java that runs games written in Javascript through Nashorn and LWJGL. A Kondion game is completely separated from the engine; no compilation is required.

The world consists of a tree of nodes, functioning as components of the game.
There are three base classes:
+ **KObj_Node**				(NKO, GKO)	- Can be placed in the world tree.
+ **KObj_Oriented**		(OKO)				- Has a position and rotation (matrix).
+ **KObj_Rendereble**	(RKO)				- Based on Oriented, can be rendered.
+ **Kobj_Solid**			(SKO)				- Based on Oriented, can be rendered and is solid.

An oriented object moves and rotates along with it's parent, the same way Blender does.

The objects that make the world:
+ **GKO_Client**				- Incomplete multiplayer client
+ **GKO_DeferredPass**	- Deferred rendering pass
+ **GKO_RenderPass**		- A render pass that renders either Default, Diffuse, Depth, Normals, Brightness, or GUI.
+ **GKO_Scene**					- Pretty much the world itself
+ **GKO_Server**				- Incomplete multiplayer server
+ **NKO_Audio**					- Linked to a sound resource, plays sounds. Position of sound depends on parent.
+ **OKO_Camera_**				- Positions the camera in the world
+ **RKO_Board**					- A boring flat 1x1 plane
+ **RKO_DirectionalLight**	- A light where the light rays travel only one direction, position doesn't matter.
+ **RKO_GUI**						- Unused, use compositor and renderlayer buttons
+ **RKO_AmbientLight**	- Lights up every surface 
+ **RKO_Obj**						- A static 3D model
+ **SKO_Cube**					- A neat little cube
+ **SKO_InfinitePlane**	- A plane that never ends

Also, materials for renderables:
+ **Mat_FlatColor**		- Flat color, what else?
+ **Mat_Monotexture**	- Single texture material
+ **Mat_Strange**		- Test texture, warning: strange

The entire game is controled by Javascript functions stored in the 's' property of the nodes.

For example:

    cube.s.onupdate = function() {...}

Supported resources:
+ **3D model** (obj)
+ **Audio** (ogg)
+ **Texture** (png, some other formats that I didn't know were supported)

Resources and other information are stored in the **kondion.json** file.

Additional Features:
+ Multiple render passes with compositing
+ Rendering context with similar syntax to the HTML5 canvas 2D context (fillRect, drawImage)
+ Timescale feature
+ JOML accesable through JS, for in-game vector/matrix math
+ Choice beteween fixed framerate, or delta timing
+ a few useful functions, like hitscanning

See test game 1 for more info.

K[name here] is only used in the source code and files to avoid conflict with KDE software.
This entire engine is currently being rewritten in C++ with embedded Chrome V8.

Screenshots
==================================

![SS00](http://vendalenger.x10host.com/img/Kondion_Screen00.png)
![SS01](http://vendalenger.x10host.com/img/Kondion_Screen01.png)

Lisenses
==================================

    Copyright 2015 Neal Nicdao

    Licensed under the Apache License, Version 2.0 (the "License"); you may not
    use this file except in compliance with the License. You may obtain a copy of
    the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
    WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
    License for the specific language governing permissions and limitations under
    the License.

============

    Argo: argo-3.12.jar
    released under Apache License, Version 2.0
    A fast and simple json parser and formatter for Java.
    by: mos20
    http://sourceforge.net/projects/argo/


    LWJGL: lwjgl*
    http://www.lwjgl.org/license
    LWJGL is a Java library that enables cross-platform
    access to popular native APIs usefulin the development
    of graphics (OpenGL), audio (OpenAL) and parallel
    computing (OpenCL) applications.
    http://www.lwjgl.org/


    Zip4j zip4j_1.3.2.jar
    released under Apache License, Version 2.0
    Zip4j is a simple to use, opensource java library that
    supports creating, extracting, updating Zip files.
    by: Srikanth Reddy Lingala


    The MIT License (MIT)

    Copyright (c) 2015-2016 JOML
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
